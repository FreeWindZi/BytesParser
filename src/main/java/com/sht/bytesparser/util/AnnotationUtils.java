package com.sht.bytesparser.util;

import com.sht.bytesparser.annotation.BytesInfo;
import com.sht.bytesparser.annotation.BytesSerializable;
import com.sht.bytesparser.bean.PrimitiveProcessor;
import com.sht.bytesparser.bean.PrimitiveType;
import com.sht.bytesparser.bean.Reserved;
import com.sht.bytesparser.bean.TypeConst;
import com.sht.bytesparser.exception.AnnotationException;
import com.sht.bytesparser.exception.InternalException;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

public class AnnotationUtils {

    public static Set<Class<? extends BytesSerializable>> ANNOTATION_CHECK_CACHE = new HashSet<>();
    private static final Comparator<Field> ORDER_COMPARATOR = new Comparator<Field>() {
        @Override
        public int compare(Field left, Field right) {
            return left.getAnnotation(BytesInfo.class).order() - right.getAnnotation(BytesInfo.class).order();
        }
    };
    public static void checkAnnotationOrThrow(Class<? extends BytesSerializable> clazz) throws AnnotationException {
        if (ANNOTATION_CHECK_CACHE.contains(clazz)){
            return;
        }
        Set<Class<? extends BytesSerializable>> recursiveTypeSet = new HashSet<>();
        checkAnnotationOrThrow(clazz, false, recursiveTypeSet);
        ANNOTATION_CHECK_CACHE.add(clazz);
    }

    public static List<Field> getSortedBytesInfo(Class<?> clazz) {
        List<Field> paramFields = ReflectionUtils.extractAnnotatedFields(clazz, BytesInfo.class);
        Collections.sort(paramFields, ORDER_COMPARATOR);
        return paramFields;
    }

    private static void checkAnnotationOrThrow(Class<? extends BytesSerializable> clazz, boolean isRecursive,
                                              Set<Class<? extends BytesSerializable>> recursiveTypeSet){
        if (! ReflectionUtils.isNonParamConstructorExists(clazz)) {
            throw new AnnotationException("Class [" + clazz.getName() + "] should define a non-parameter constructor");
        }
        if (recursiveTypeSet.contains(clazz)) {
            throw new AnnotationException("Recursive TairaData type " + clazz.getName() + " already exists");
        }
        List<Field> fields = ReflectionUtils.extractAnnotatedFields(clazz, BytesInfo.class);
        if (fields.isEmpty()){
            throw new AnnotationException("No @BytesInfo declared in class [" + clazz.getName() + "]");
        }
        Collections.sort(fields, ORDER_COMPARATOR);
        int size = fields.size();
        for (int i = 0; i < size; i++){
            Field field = fields.get(i);
            BytesInfo info = field.getAnnotation(BytesInfo.class);
            Class fieldType = field.getType();
            //first check order
            checkFieldOrder(clazz, field, i, info.order());

            //check basic type : Java PrimitiveType; Reserved; BytesSerializable

            //continue, check Reserved.class
            if (fieldType == Reserved.class){
                continue;
            }
            //continue, check PrimitiveType
            PrimitiveType primitiveType = TypeConst.findPrimitive(fieldType);
            if (primitiveType != null){
                checkPrimitiveType(clazz, field, primitiveType, info);
                continue;
            }
            //continue, check BytesSerializable
            if (TypeConst.isBytesSerializableClass(fieldType)) {
                recursiveTypeSet.add(clazz);
                checkAnnotationOrThrow(fieldType, true, recursiveTypeSet);
                continue;
            }



            // non Fix Attribute
            if (isSupportNonFixType(fieldType) && info.len() == BytesInfo.INVALID_LEN){
                if (info.lenFlagBytesSize() > 0){
                }else {
                    throw new AnnotationException("Field [" + field.getName()+ "] "+
                        "@BytesInfo lenFlagBytesSize must be greater than 0 in class [ " + clazz.getName()+ "]") ;
                }
            }


            //check advanced type : String; Array; Collection
            if (TypeConst.isSupportedCollection(fieldType) || fieldType.isArray()) {
                Class memberType = ReflectionUtils.getCollectionFirstMemberType(field);

                PrimitiveType memberPrimitiveType = TypeConst.findPrimitive(memberType);
                if (memberPrimitiveType != null){
                    //checkPrimitiveType(clazz, field, memberPrimitiveType, info.len());
                    continue;
                }

                // abstract or interface member type not supported
                if (ReflectionUtils.isInterfaceOrAbstract(memberType)) {
                    throw new AnnotationException(
                            "Member type of collection field [" + field.getName() + "] in class [" + clazz.getName()
                                    + "] should not be interface or abstract");
                }
                // TairaData member type, recursively check
                if (TypeConst.isBytesSerializableClass(memberType)) {
                    recursiveTypeSet.add(clazz);
                    checkAnnotationOrThrow(memberType, true, recursiveTypeSet);
                } else {
                    PrimitiveProcessor memberProcessor = TypeConst.findPrimitive(memberType);
                    // unsupported member type
                    if (memberProcessor == null) {
                        throw new AnnotationException(
                                "Member type of collection field [" + field.getName() + "] in class [" + clazz.getName()
                                        + "] can only be primitive type or TairaData");
                    }
                }
                continue;
            }


        }
    }




    private static void checkFieldOrder(Class clazz, Field field,int fieldIndex, int order) {
        if (fieldIndex != order) {
            throw new AnnotationException(
                    "[order] on field [" + field.getName() + "] in class [" + clazz.getName() + "] is not sequential");
        }
    }

    private static void checkPrimitiveType(Class clazz, Field field, PrimitiveType primitiveType, BytesInfo info ) {

        if (primitiveType != null){
            int realByteSize = evaluatePrimitiveSize(primitiveType, info.len());
            if (realByteSize > primitiveType.byteSize()){
                throw new AnnotationException("[bytes] on field [" + field.getName() + "] in class [" + clazz.getName()
                        + "] is too large (which should be lesser than or equal to " + primitiveType.byteSize() + ")");
            }

            if (realByteSize == primitiveType.byteSize() && !info.sign()){
                throw new AnnotationException("[bytes] on field [" + field.getName() + "] in class [" + clazz.getName()
                        + "] is too large (which should be lesser than" + primitiveType.byteSize() + ")" +
                        "when @BytesInfo sign = false");
            }
        }

    }

    private static boolean isSupportNonFixType(Class fieldType){
        if (fieldType == String.class || TypeConst.isSupportedCollection(fieldType) || fieldType.isArray()){
            return true;
        }else {
            return false;
//            throw new AnnotationException("Type of field [" + field.getName() + "] " +
//                    "in class [" + fieldType.getName() + "] is not supported when using nonFix attribute");
        }
    }

//    private static void checkExistRelativeField(Class clazz, Field field, Field lastField){
//        String relativeFieldName = field.getName()+BytesInfo.LEN_FLAG_SUFFIX;
//        if (lastField == null){
//            throw new AnnotationException("RelativeField [" + relativeFieldName + "]" + "must exist , and be front of the Field [" + field+"]" +
//                    "in class [ " +clazz.getName() +"]");
//        }
//        if(!lastField.getName().equals(relativeFieldName)){
//            throw new AnnotationException("RelativeField [" + relativeFieldName + "]" + "must exist , and be front of the Field [" + field+"]" +
//                    "in class [ " +clazz.getName() +"]");
//        }
//        BytesInfo info = lastField.getAnnotation(BytesInfo.class);
//        if (!info.lenFlag()){
//            throw new AnnotationException("@BytesInfo  lenFlag() must ture in field [" +relativeFieldName +"]" +
//                    "in class [ " +clazz.getName() +"]");
//        }
//
//    }

//    private static void checkNonFix()


    public static boolean isNonFixLength(Class<? extends BytesSerializable> clazz){
        List<Field> fields = getSortedBytesInfo(clazz);
        for (Field field : fields){
            BytesInfo info = ReflectionUtils.getAnnotation(field, BytesInfo.class);
            if (info.lenFlag()){
                return true;
            }
            Class fieldType = field.getType();
            if (TypeConst.isBytesSerializableClass(fieldType) && isNonFixLength(fieldType)){
                return true;
            }
        }
        return false;
    }


    public static int getMinByteSize(Class<? extends BytesSerializable> clazz){
        int byteSize = 0;
        List<Field> fields = getSortedBytesInfo(clazz);
        for (Field field : fields){
            Class fieldType = field.getType();
            BytesInfo bytesInfo = ReflectionUtils.getAnnotation(field, BytesInfo.class);
            PrimitiveType primitiveType = TypeConst.findPrimitive(fieldType);
            if (primitiveType != null){
                byteSize += evaluatePrimitiveSize(primitiveType, bytesInfo.len());
                continue;
            }
            if (fieldType == Reserved.class){
                byteSize += bytesInfo.len();
                continue;
            }
            if (TypeConst.isBytesSerializableClass(fieldType)){
                byteSize += getMinByteSize(fieldType);
                continue;
            }

            if (fieldType == String.class){
                byteSize += bytesInfo.len();
                continue;
            }

            if (TypeConst.isSupportedCollection(fieldType) || fieldType.isArray()) {
                Class memberType = ReflectionUtils.getCollectionFirstMemberType(field);
                if (bytesInfo.len() > 0) {
                    PrimitiveType memberPrimitiveType = TypeConst.findPrimitive(memberType);
                    if (memberPrimitiveType != null) {
                        byteSize += (bytesInfo.len() * evaluatePrimitiveSize(memberPrimitiveType, bytesInfo.unitLen()));
                        continue;
                    }
                    if (TypeConst.isBytesSerializableClass(memberType)) {
                        byteSize += (bytesInfo.len() * getMinByteSize(memberType));
                        continue;
                    }
                    throw new InternalException("memberType [" + memberType.getName() + "]" +
                            " is not support");
                }
            }
        }
        return byteSize;
    }

    public static int evaluatePrimitiveSize(PrimitiveType type, int annotationLength) {
        int realLength = type.byteSize();
        if (annotationLength <= 0 || realLength <= 1 || realLength == annotationLength) {
            return realLength;
        }
        return annotationLength;
    }

}
