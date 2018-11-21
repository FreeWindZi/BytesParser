package com.sht.bytesparser;

import com.sht.bytesparser.annotation.BitsInfo;
import com.sht.bytesparser.annotation.BytesInfo;
import com.sht.bytesparser.log.LoggerUtil;
import com.sht.bytesparser.parser.JavaDataType;
import com.sht.bytesparser.parser.JavaTypeProvide;
import com.sht.bytesparser.parser.interf.ParsableType;
import com.sht.bytesparser.parser.ParsedResult;
import com.sht.bytesparser.util.BytesParserUtils;
import com.sht.bytesparser.util.CompatUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import static com.sht.bytesparser.annotation.BytesInfo.LEN_FLAG_SUFFIX;

public class BytesParser {
    private ByteOrder order;

    private String charsetName = "UTF-8";

    JavaTypeProvide typeProvide = new JavaTypeProvide();

    private BytesParser() {
    }


    private BytesParser(BytesParser origin) {
        this.order = origin.order;
        this.charsetName = origin.charsetName;
        this.typeProvide = origin.typeProvide;
    }

    public static class Builder {

        private BytesParser target;

        public Builder() {
            target = new BytesParser();
        }

        public Builder order(ByteOrder order) {
            target.order = order;
            return this;
        }

        public Builder charsetName(String charsetName) {
            target.charsetName = charsetName;
            return this;
        }

        public Builder addJavaType(Class<?> clz, JavaDataType javaDataType) {
            target.typeProvide.addJavaType(clz, javaDataType);
            return this;
        }

        public BytesParser build() {
            return new BytesParser(target);
        }

    }


//==========================================实现 code =====================


    public <T> T toBean(Class<T> clz, byte[] bytes) {
        return toBean(clz, bytes, 0, bytes.length);
    }

    public <T> T toBean(Class<T> clz, byte[] bytes, int start) {
        List<Field> fields = checkAndGetFields(clz);
        int length = getByteLength(fields);
        return toBean(clz, bytes, start, length);
    }

    public <T> T toBean(Class<T> clz, byte[] bytes, int start, int length) {
        //判断注解
        List<Field> fields = checkAndGetFields(clz);
//        int length = checkAndGetByteLength(fields, start, bytes.length);
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes, start, length);
        return toBean(clz, fields, byteBuffer, start, length);
    }

    public <T> T toBean(Class<T> clz, ByteBuffer byteBuffer) {
        List<Field> fields = checkAndGetFields(clz);
        int start = byteBuffer.position();
        int limite = byteBuffer.limit();
        return toBean(clz, fields, byteBuffer, start, limite - start);
    }

    public <T> T toBean(Class<T> clz, ByteBuffer byteBuffer, int start, int length) {
        List<Field> fields = checkAndGetFields(clz);
        byteBuffer.position(start);
        byteBuffer.limit(start + length);
        return toBean(clz, fields, byteBuffer, start, length);
    }


    private <T> T toBean(Class<T> clz, List<Field> fields, ByteBuffer byteBuffer, int start, int length) {
        T object = BytesParserUtils.newInstance(clz);
        byteBuffer.order(order);
        int beginPos = start;
        for (Field field : fields) {
            if (beginPos >= start + length) {
                LoggerUtil.e("Ran out of bytes to parseBytesToBean, total byte size=["
                        + length + " but trying to parseBytesToBean field[" + field + "]");
                break;
            }
            byteBuffer.position(beginPos);
            ParsableType parsableType = typeProvide.getParsableType(field.getType());

            ParsedResult result = parsableType.parseBytesToBean(object, field, byteBuffer, beginPos);
            BytesParserUtils.setFieldValue(object, field, result.getValue());
            beginPos += result.getBytesUsed();

        }
        System.out.println();
        return object;
    }


    //转换成二进制

    public byte[] toBytes(Object object) {
        Class<?> clz = object.getClass();
        List<Field> fields = checkAndGetFields(clz);
        List<Field> needInitField = BytesParserUtils.getNeedInitField(fields);
        int needAddLen = 0;
        for (Field field : needInitField) {
            //去掉后缀，并获取关联的Field
            Field relateField = null;
            Object relateValue = null;
            try {
                relateField = clz.getDeclaredField(field.getName().substring(0, field.getName().indexOf(LEN_FLAG_SUFFIX)));
                relateValue = BytesParserUtils.getFieldValue(object, relateField);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                throw new IllegalArgumentException(String.format("%s has no ralate Field, and the ralate Field must be named %s"
                        , field.getName(), field.getName().substring(0, field.getName().indexOf(LEN_FLAG_SUFFIX))));
            }
            Integer needInitFieldValueInt = (Integer) typeProvide.getParsableType(relateField.getType())
                    .getNeedInitFieldValue(relateField, relateValue, field);

            needAddLen += needInitFieldValueInt;
            Object needInitFieldValue = null;
            if (JavaDataType.BYTE_TYPE.contains(field.getType())) {
                needInitFieldValue = needInitFieldValueInt.byteValue();
            } else if (JavaDataType.SHORT_TYPE.contains(field.getType())) {
                needInitFieldValue = needInitFieldValueInt.shortValue();
            } else if (JavaDataType.CHAR_TYPE.contains(field.getType())) {
                short value = needInitFieldValueInt.shortValue();
                char charValue = (char) (value);
                needInitFieldValue = charValue;
            } else {
                needInitFieldValue = needInitFieldValueInt;
            }
            BytesParserUtils.setFieldValue(object, field, needInitFieldValue);
        }

        int length = getByteLength(fields);
        length += needAddLen;
        byte dst[] = new byte[length];
        toBytes(object, dst, 0, length);
        return dst;
    }

    public void toBytes(Object object, byte dst[], int start, int length) {
        Class<?> clz = object.getClass();
        List<Field> fields = checkAndGetFields(clz);
        ByteBuffer byteBuffer = ByteBuffer.wrap(dst, start, length);
        byteBuffer.order(order);
        int beginPos = start;
        for (Field field : fields) {
            byteBuffer.position(beginPos);
            Object fieldValue = BytesParserUtils.getFieldValue(object, field);
            ParsableType parsableType = typeProvide.getParsableType(field.getType());
            ParsedResult result = parsableType.parseBeanToBytes(field, fieldValue, byteBuffer, beginPos);
            beginPos += result.getBytesUsed();
        }

    }

    public ByteBuffer parseBeanToByteBuffer(Object object) {
        Class<?> clz = object.getClass();


        return null;
    }


    //=====================================check

    private List<Field> checkAndGetFields(Class<?> clz) {
        List<Field> fields = BytesParserUtils.getParsableFieldsSortedByOrder(clz);
        if (fields == null || fields.size() == 0) {
            throw new IllegalArgumentException("No field annotated with ElementField is found in ["
                    + clz.getName() + "]");
        }
        return fields;
    }

    private int checkAndGetByteLength(int start, int length, int totalByteLength) {
        if (start + length > totalByteLength) {
            throw new IllegalArgumentException("the length of bytes is smaller than need");
        }
        return length;
    }


    public int getByteLength(Class<?> clz){
        List<Field> fields = checkAndGetFields(clz);
        return getByteLength(fields);
    }

    private int getByteLength(List<Field> fields) {

        int bitLength = 0;
        for (Field field : fields) {
            BytesInfo info = CompatUtils.getDeclaredAnnotation(field, BytesInfo.class);
            if (info == null) {
                return CompatUtils.getDeclaredAnnotation(field, BitsInfo.class).len();
            }

            if (!field.getType().isArray()) {
                if (info.len() == BytesInfo.INVALID_LEN) {
                    Integer defaultLen = (Integer) CompatUtils.getOrDefault(JavaDataType.JAVA_DATA_TYPE_SIZE, field.getType(), 0);
                    bitLength += defaultLen;
                } else {
                    bitLength += info.len() * 8;
                }
            } else {
                //处理数组
                Class<?> componentType = field.getType().getComponentType();
                if (componentType.isArray()){
                    throw new UnsupportedOperationException("just support one dimensional array");
                }
                if (info.len() == BytesInfo.INVALID_LEN){
                    throw new IllegalArgumentException("just suppor fix length array");
                }else {
                    int uintLen = 0;
                    if (info.unitLen() == BytesInfo.INVALID_LEN){
                        uintLen = (Integer) CompatUtils.getOrDefault(JavaDataType.JAVA_DATA_TYPE_SIZE, componentType, 0);
                    }else {
                        uintLen = info.unitLen() * 8;
                    }
                    bitLength += (info.len() * uintLen);
                }

            }
        }
        if (bitLength % 8 != 0) {
            throw new IllegalArgumentException("the length of all BitsInfos must be be divided exactly by eight");
        }
        return bitLength / 8;
    }

    public static void main(String argv[]) {
        LoggerUtil.d("顾大大");
    }
}
