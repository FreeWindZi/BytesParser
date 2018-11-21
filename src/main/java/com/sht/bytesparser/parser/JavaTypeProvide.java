package com.sht.bytesparser.parser;

import com.sht.bytesparser.parser.bean.Reserved;
import com.sht.bytesparser.parser.interf.ParsableType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by navy on 2018/11/12.
 */

public class JavaTypeProvide {

    public static final JavaDataType.ByteJavaDataType byteJavaDataType = new JavaDataType.ByteJavaDataType();
    public static final JavaDataType.ShortJavaDataType shortJavaDataType = new JavaDataType.ShortJavaDataType();
    public static final JavaDataType.CharJavaDataType charJavaDataType = new JavaDataType.CharJavaDataType();
    public static final JavaDataType.IntJavaDataType intJavaDataType = new JavaDataType.IntJavaDataType();
    public static final JavaDataType.LongJavaDataType longJavaDataType = new JavaDataType.LongJavaDataType();
    public static final JavaDataType.FloatJavaDataType floatJavaDataType = new JavaDataType.FloatJavaDataType();
    public static final JavaDataType.DoubleJavaDataType doubleJavaDataType = new JavaDataType.DoubleJavaDataType();

    public static final Map<Class<?>, JavaDataType> BASE_DATA_TYPES = new HashMap<>();
    public static final Map<Class<?>, Integer> JAVA_DATA_TYPE_SIZE = new HashMap<>();
    static  {

        BASE_DATA_TYPES.put(Byte.TYPE, byteJavaDataType);
        BASE_DATA_TYPES.put(Byte.class, byteJavaDataType);

        BASE_DATA_TYPES.put(Short.TYPE, shortJavaDataType);
        BASE_DATA_TYPES.put(Short.class, shortJavaDataType);

        BASE_DATA_TYPES.put(Character.TYPE, charJavaDataType);
        BASE_DATA_TYPES.put(Character.class, charJavaDataType);

        BASE_DATA_TYPES.put(Integer.TYPE, intJavaDataType);
        BASE_DATA_TYPES.put(Integer.class, intJavaDataType);

        BASE_DATA_TYPES.put(Long.TYPE, longJavaDataType);
        BASE_DATA_TYPES.put(Long.class, longJavaDataType);

        BASE_DATA_TYPES.put(Float.TYPE, floatJavaDataType);
        BASE_DATA_TYPES.put(Float.class, doubleJavaDataType);

        BASE_DATA_TYPES.put(Double.TYPE, new JavaDataType.DoubleJavaDataType());
        BASE_DATA_TYPES.put(Double.class, new JavaDataType.DoubleJavaDataType());

        BASE_DATA_TYPES.put(Reserved.class, new ReservedJavaDataType());
        BASE_DATA_TYPES.put(String.class, new StringJavaDataType());
        BASE_DATA_TYPES.put(byte[].class, new ArrayJavaDataType());
        BASE_DATA_TYPES.put(short[].class, new ArrayJavaDataType());
        BASE_DATA_TYPES.put(char[].class, new ArrayJavaDataType());
        BASE_DATA_TYPES.put(int[].class, new ArrayJavaDataType());
        BASE_DATA_TYPES.put(long[].class, new ArrayJavaDataType());
        BASE_DATA_TYPES.put(byte[].class, new ArrayJavaDataType());
        BASE_DATA_TYPES.put(float[].class, new ArrayJavaDataType());
        BASE_DATA_TYPES.put(double[].class, new ArrayJavaDataType());
///============================================map size init==========================
        JAVA_DATA_TYPE_SIZE.put(Boolean.TYPE, 1);
        JAVA_DATA_TYPE_SIZE.put(Boolean.class, 1);

        JAVA_DATA_TYPE_SIZE.put(Byte.TYPE, Byte.SIZE);
        JAVA_DATA_TYPE_SIZE.put(Byte.class, Byte.SIZE);

        JAVA_DATA_TYPE_SIZE.put(Short.TYPE, Short.SIZE);
        JAVA_DATA_TYPE_SIZE.put(Short.class, Short.SIZE);

        JAVA_DATA_TYPE_SIZE.put(Character.TYPE, Character.SIZE);
        JAVA_DATA_TYPE_SIZE.put(Character.class, Character.SIZE);

        JAVA_DATA_TYPE_SIZE.put(Integer.TYPE, Integer.SIZE);
        JAVA_DATA_TYPE_SIZE.put(Integer.class, Integer.SIZE);

        JAVA_DATA_TYPE_SIZE.put(Long.TYPE, Long.SIZE);
        JAVA_DATA_TYPE_SIZE.put(Long.class, Long.SIZE);

        JAVA_DATA_TYPE_SIZE.put(Float.TYPE, Float.SIZE);
        JAVA_DATA_TYPE_SIZE.put(Float.class, Float.SIZE);

        JAVA_DATA_TYPE_SIZE.put(Double.TYPE, Double.SIZE);
        JAVA_DATA_TYPE_SIZE.put(Double.class, Double.SIZE);
    }


    public  Map<Class<?>, ParsableType> JAVA_DATA_TYPES = new HashMap<>();

    public JavaTypeProvide() {
        JAVA_DATA_TYPES.putAll(BASE_DATA_TYPES);
    }

    public JavaTypeProvide addJavaType(Class<?> clz, ParsableType parsableType){
        JAVA_DATA_TYPES.put(clz, parsableType);
        return this;
    }

    public ParsableType getParsableType(Class<?> clazz) {
        ParsableType parsableType = JAVA_DATA_TYPES.get(clazz);
        if (parsableType != null){
            return parsableType;
        }else {
            throw new UnsupportedOperationException(clazz.getName() + " is unsupported");
        }
    }




}
