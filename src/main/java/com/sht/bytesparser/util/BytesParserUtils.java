package com.sht.bytesparser.util;

import com.sht.bytesparser.annotation.BitsInfo;
import com.sht.bytesparser.annotation.BytesInfo;
import com.sht.bytesparser.annotation.OrderInfo;
import com.sht.bytesparser.parser.JavaDataType;
import com.sht.bytesparser.parser.JavaTypeProvide;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sht.bytesparser.parser.JavaDataType.CHAR_TYPE;
import static com.sht.bytesparser.parser.JavaDataType.FLOATS_TYPE;
import static com.sht.bytesparser.parser.JavaDataType.INTEGERS_TYPE;
import static com.sht.bytesparser.parser.JavaDataType.INT_TYPE;
import static com.sht.bytesparser.parser.JavaDataType.JAVA_DATA_TYPE_SIZE;
import static com.sht.bytesparser.parser.JavaDataType.LONG_TYPE;
import static com.sht.bytesparser.parser.JavaDataType.SHORT_TYPE;

public class BytesParserUtils {

    public static List<Field> getParsableFieldsSortedByOrder(Class<?> type) {

        List<Field> validFields = new ArrayList<>();
        for (Field field: type.getDeclaredFields()) {
            if (field.isAnnotationPresent(BytesInfo.class) || field.isAnnotationPresent(BitsInfo.class)){
                validFields.add(field);
            }
        }
        Collections.sort(validFields, new Comparator<Field>() {
            @Override
            public int compare(Field field1, Field field2) {
                int order1 ;
                BytesInfo info1 =  CompatUtils.getDeclaredAnnotation(field1, BytesInfo.class);
                if (info1 != null){
                    order1 = info1.order();
                }else {
                    order1 =CompatUtils.getDeclaredAnnotation(field1, BitsInfo.class).order();
                }

                int order2 ;
                BytesInfo info2 = CompatUtils.getDeclaredAnnotation(field2, BytesInfo.class);
                if (info2 != null){
                    order2 = info2.order();
                }else {
                    order2 = CompatUtils.getDeclaredAnnotation(field2, BitsInfo.class).order();
                }
                return order1 == order2 ? 0 : (order1 > order2 ? 1 : -1);
            }
        });
        return validFields;

//        return Stream.of(type.getDeclaredFields())
//                .filter(field -> field.isAnnotationPresent(BytesInfo.class) || field.isAnnotationPresent(BitsInfo.class) )
//                .sorted((Field field1, Field field2) -> {
//                    int order1 ;
//                    BytesInfo info1 = field1.getDeclaredAnnotation(BytesInfo.class);
//                    if (info1 != null){
//                        order1 = info1.order();
//                    }else {
//                        order1 = field1.getDeclaredAnnotation(BitsInfo.class).order();
//                    }
//
//                    int order2 ;
//                    BytesInfo info2 = field2.getDeclaredAnnotation(BytesInfo.class);
//                    if (info2 != null){
//                        order2 = info2.order();
//                    }else {
//                        order2 = field2.getDeclaredAnnotation(BitsInfo.class).order();
//                    }
//                    return order1 == order2 ? 0 : (order1 > order2 ? 1 : -1);
//                }).collect(Collectors.toList());

    }


    public static List<Field> getNeedInitField(List<Field> list) {

        if (list == null){
            return null;
        }

        List<Field> needInitFields = new ArrayList<>();
        for (Field field: list) {
            BytesInfo bytesInfo = CompatUtils.getDeclaredAnnotation(field, BytesInfo.class);
            if (bytesInfo != null && bytesInfo.lenFlag() == true){
                needInitFields.add(field);
            }
        }
        return needInitFields;

//        return list.stream()
//                .filter(field -> {
//                    return field.getDeclaredAnnotation(BytesInfo.class).lenFlag();
//                }).collect(Collectors.toList());


    }


    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Fail to instantiate " + clazz.getName()
                    + ", an empty constructor is required.", e);
        }
    }

    public static void setFieldValue(Object object, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Fail to set field value[" + field.getName()
                    + "] to [" + object + "]", e);
        }
    }

    public static Object getFieldValue(Object object, Field field) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Fail to get field value[" + field.getName()
                    + "] to [" + object + "]", e);
        }
    }

    public static Object reflectMethod(Field field, String methodName, Object fieldValue){

        try {
            Method method = fieldValue.getClass().getMethod(methodName);
            return method.invoke(fieldValue);
        }  catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static JavaDataType getFitJavaDataType(Class<?> clz, int bytesLen, boolean bytesInfoSign){
        if (bytesLen == 0){
            bytesLen = (int) CompatUtils.getOrDefault(JAVA_DATA_TYPE_SIZE, clz, 0) / 8;
        }

        if (INTEGERS_TYPE.contains(clz)){
            switch (bytesLen){
                case 1:
                    return JavaTypeProvide.byteJavaDataType;
                case 2:
                    boolean sign = true;
                    if (CHAR_TYPE.contains(clz)){
                        sign = false;
                    }else if (SHORT_TYPE.contains(clz)){
                        sign = true;
                    } else if (INT_TYPE.contains(clz)
                            || LONG_TYPE.contains(clz)){
                        sign = bytesInfoSign;
                    }
                    if (sign){
                        return JavaTypeProvide.shortJavaDataType;
                    }else {
                        return JavaTypeProvide.charJavaDataType;
                    }
                case 4:
                    return JavaTypeProvide.intJavaDataType;

                case 8:
                    return JavaTypeProvide.longJavaDataType;
                default:
                    throw new IllegalArgumentException("Integer data len not right " + bytesLen );
            }
        } else if (FLOATS_TYPE.contains(clz)){
            switch (bytesLen){
                case 4:
                    return JavaTypeProvide.floatJavaDataType;
                case 8:
                    return JavaTypeProvide.doubleJavaDataType;
                default:
                    throw new IllegalArgumentException("float data len not right " + bytesLen);
            }
        }
        throw new UnsupportedOperationException(clz.getName() + " is unsupported");
    }
}
