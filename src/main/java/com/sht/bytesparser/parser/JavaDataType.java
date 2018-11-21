package com.sht.bytesparser.parser;


import com.sht.bytesparser.annotation.BytesInfo;
import com.sht.bytesparser.parser.interf.ParsableType;
import com.sht.bytesparser.util.BytesParserUtils;
import com.sht.bytesparser.util.CompatUtils;


import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by navy on 2018/11/10.
 */

public abstract class JavaDataType implements ParsableType {


    public static final Map<Class<?>, Integer> JAVA_DATA_TYPE_SIZE = new HashMap<>();

    public static final List<Class<?>> INTEGERS_TYPE = new ArrayList<>();
    public static final List<Class<?>> FLOATS_TYPE = new ArrayList<>();

    public static final List<Class<?>> BYTE_TYPE = new ArrayList<>();
    public static final List<Class<?>> CHAR_TYPE = new ArrayList<>();
    public static final List<Class<?>> SHORT_TYPE = new ArrayList<>();
    public static final List<Class<?>> INT_TYPE = new ArrayList<>();
    public static final List<Class<?>> LONG_TYPE = new ArrayList<>();

    public static final List<Class<?>> FLOAT_TYPE = new ArrayList<>();
    public static final List<Class<?>> DOUBLE_TYPE = new ArrayList<>();
    static {
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

        INTEGERS_TYPE.add(Boolean.TYPE);
        INTEGERS_TYPE.add(Boolean.class);
        INTEGERS_TYPE.add(Byte.TYPE);
        INTEGERS_TYPE.add(Byte.class);
        INTEGERS_TYPE.add(Short.TYPE);
        INTEGERS_TYPE.add(Short.class);
        INTEGERS_TYPE.add(Character.TYPE);
        INTEGERS_TYPE.add(Character.class);
        INTEGERS_TYPE.add(Integer.TYPE);
        INTEGERS_TYPE.add(Integer.class);
        INTEGERS_TYPE.add(Long.TYPE);
        INTEGERS_TYPE.add(Long.class);

        FLOATS_TYPE.add(Float.TYPE);
        FLOATS_TYPE.add(Float.class);
        FLOATS_TYPE.add(Double.TYPE);
        FLOATS_TYPE.add(Double.class);

        BYTE_TYPE.add(Byte.TYPE);
        BYTE_TYPE.add(Byte.class);
        CHAR_TYPE.add(Character.TYPE);
        CHAR_TYPE.add(Character.class);
        SHORT_TYPE.add(Short.TYPE);
        SHORT_TYPE.add(Short.class);
        INT_TYPE.add(Integer.TYPE);
        INT_TYPE.add(Integer.class);
        LONG_TYPE.add(Long.TYPE);
        LONG_TYPE.add(Long.class);

        FLOAT_TYPE.add(Float.TYPE);
        FLOAT_TYPE.add(Float.class);

        DOUBLE_TYPE.add(Double.TYPE);
        DOUBLE_TYPE.add(Double.class);

    }
    @Override
    public ParsedResult parseBytesToBean(Object object, Field field, ByteBuffer byteBuffer, int beginPos) {
        byteBuffer.position(beginPos);

        byteBuffer.position(beginPos);
        BytesInfo info = CompatUtils.getDeclaredAnnotation(field, BytesInfo.class);
        if (info == null){
            throw new NullPointerException("now must support BytesInfo");
        }

        return BytesParserUtils.getFitJavaDataType(field.getType(), info.len(), info.sign())
                .parseToBean(field, byteBuffer, beginPos);
    }





    @Override
    public ParsedResult parseBeanToBytes(Field field, Object fieldValue, ByteBuffer byteBuffer, int beginPos) {
        byteBuffer.position(beginPos);
        BytesInfo info = CompatUtils.getDeclaredAnnotation(field, BytesInfo.class);
        if (info == null){
            throw new NullPointerException("now must support BytesInfo");
        }

        return BytesParserUtils.getFitJavaDataType(field.getType(), info.len(), info.sign())
                .parseToBytes(field, fieldValue, byteBuffer, beginPos);
    }




    abstract ParsedResult parseToBean(Field field, ByteBuffer byteBuffer, int beginPos);
    abstract ParsedResult parseToBytes(Field field, Object fieldValue, ByteBuffer byteBuffer, int beginPos);

    @Override
    public Object getNeedInitFieldValue(Field relateField, Object fieldValue, Field field) {
        return null;
    }

    @Override
    public Object getRelateFieldValue(Object object, Field field) {
        return null;
    }
    //    public static class BooleanJavaDataType extends JavaDataType{
//        @Override
//        public ParsedResult parseBytesToBean(Field field, ByteBuffer byteBuffer, int beginPos) {
//            BitsInfo info = field.getDeclaredAnnotation(BitsInfo.class);
//            if (info == null){
//                throw new  NullPointerException("Byte must use BytesInfo Annotation");
//            }else if(info.len() != BytesInfo.INVALID_LEN && info.len() != 1) {
//                throw new  IllegalArgumentException("the length of BytesInfo must be -1 or 1 when using byte type ");
//            }
//            byte value =  byteBuffer.get(beginPos);
//            return new ParsedResult(value, Byte.SIZE);
//        }
//    }

    public static class ByteJavaDataType extends JavaDataType{
        @Override
        public ParsedResult parseToBean(Field field, ByteBuffer byteBuffer, int beginPos) {
            byte value =  byteBuffer.get(beginPos);
            return new ParsedResult(value, Byte.SIZE);
        }

        @Override
        ParsedResult parseToBytes(Field field, Object fieldValue, ByteBuffer byteBuffer, int beginPos) {
            Object value =BytesParserUtils.reflectMethod(field, "byteValue", fieldValue);
            byteBuffer.put((Byte) value);
            return new ParsedResult(null, Byte.SIZE);
        }
    }
    public static class CharJavaDataType extends JavaDataType{

        @Override
        public ParsedResult parseToBean(Field field, ByteBuffer byteBuffer, int beginPos) {
            char value = byteBuffer.getChar();
            return new ParsedResult(value, Character.SIZE);
        }
        @Override
        ParsedResult parseToBytes(Field field, Object fieldValue, ByteBuffer byteBuffer, int beginPos) {
            if (CHAR_TYPE.contains(fieldValue.getClass())){
                byteBuffer.putChar((char) fieldValue);
            } else {
                short value = (short) BytesParserUtils.reflectMethod(field, "shortValue", fieldValue);
                byteBuffer.putChar((char) value);
            }


            return new ParsedResult(null, Character.SIZE);
        }
    }
    public static class ShortJavaDataType extends JavaDataType{

        @Override
        public ParsedResult parseToBean(Field field, ByteBuffer byteBuffer, int beginPos) {
            short value =  byteBuffer.getShort(beginPos);
            return new ParsedResult(value, Short.SIZE);
        }
        @Override
        ParsedResult parseToBytes(Field field, Object fieldValue, ByteBuffer byteBuffer, int beginPos) {
            Object value =BytesParserUtils.reflectMethod(field, "shortValue", fieldValue);
            byteBuffer.putShort((Short) value);
            return new ParsedResult(null, Short.SIZE);
        }

    }
    public static class IntJavaDataType extends JavaDataType{

        @Override
        public ParsedResult parseToBean(Field field, ByteBuffer byteBuffer, int beginPos) {
            int value =  byteBuffer.getInt(beginPos);
            return new ParsedResult(value, Integer.SIZE);
        }
        @Override
        ParsedResult parseToBytes(Field field, Object fieldValue, ByteBuffer byteBuffer, int beginPos) {
            Object value =BytesParserUtils.reflectMethod(field, "intValue", fieldValue);
            byteBuffer.putInt((Integer) value);
            return new ParsedResult(null, Integer.SIZE);
        }

    }

    //============================
    public static class LongJavaDataType extends JavaDataType{

        @Override
        public ParsedResult parseToBean(Field field, ByteBuffer byteBuffer, int beginPos) {
            long value =  byteBuffer.getLong(beginPos);
            return new ParsedResult(value, Long.SIZE);
        }
        @Override
        ParsedResult parseToBytes(Field field, Object fieldValue, ByteBuffer byteBuffer, int beginPos) {
            byteBuffer.putLong((Long) fieldValue);
            return new ParsedResult(null, Long.SIZE);
        }
    }
    public static class FloatJavaDataType extends JavaDataType{
        @Override
        public ParsedResult parseToBean(Field field, ByteBuffer byteBuffer, int beginPos) {
            float value =  byteBuffer.getFloat(beginPos);
            return new ParsedResult(value, Float.SIZE);
        }
        @Override
        ParsedResult parseToBytes(Field field, Object fieldValue, ByteBuffer byteBuffer, int beginPos) {
            Object value =BytesParserUtils.reflectMethod(field, "floatValue", fieldValue);
            byteBuffer.putFloat((Float) value);
            return new ParsedResult(null, Float.SIZE);
        }
    }
    public static class DoubleJavaDataType extends JavaDataType{
        @Override
        public ParsedResult parseToBean(Field field, ByteBuffer byteBuffer, int beginPos) {
            double value =  byteBuffer.getDouble(beginPos);
            return new ParsedResult(value, Double.SIZE);
        }
        @Override
        ParsedResult parseToBytes(Field field, Object fieldValue, ByteBuffer byteBuffer, int beginPos) {
            byteBuffer.putDouble((Double) fieldValue);
            return new ParsedResult(null, Double.SIZE);
        }
    }




}







