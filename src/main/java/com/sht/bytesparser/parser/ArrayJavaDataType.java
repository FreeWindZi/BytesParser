package com.sht.bytesparser.parser;

import com.sht.bytesparser.BytesParser;
import com.sht.bytesparser.annotation.BytesInfo;
import com.sht.bytesparser.parser.interf.ParsableType;
import com.sht.bytesparser.util.BytesParserUtils;
import com.sht.bytesparser.util.CompatUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * Created by navy on 2018/11/15.
 */

public class ArrayJavaDataType extends JavaDataType{

    @Override
    public ParsedResult parseBytesToBean(Object object, Field field, ByteBuffer byteBuffer, int beginPos) {

        if (!field.getType().isArray()) {
            throw new IllegalArgumentException("class is not fit");
        }
        BytesInfo info = CompatUtils.getDeclaredAnnotation(field, BytesInfo.class);
        Class<?> componentType = field.getType().getComponentType();
        int len = info.len();
        Object fieldValue = BytesParserUtils.getFieldValue(object, field);

        if (fieldValue == null){
            fieldValue = Array.newInstance(componentType, info.len());
        }
        int bytesUsed = 0;
        for (int i =0; i < len; i++){
            JavaDataType javaDataType = BytesParserUtils.getFitJavaDataType(componentType, info.unitLen(), info.sign());
            ParsedResult result = javaDataType.parseToBean(field,  byteBuffer, beginPos);
            Array.set(fieldValue, i, result.value);
            beginPos += result.getBytesUsed();
            bytesUsed += result.getBytesUsed();
        }
        return new ParsedResult(fieldValue, bytesUsed * 8 );


    }

    @Override
    public ParsedResult parseBeanToBytes(Field field, Object fieldValue, ByteBuffer byteBuffer, int beginPos) {
        if (!field.getType().isArray()) {
            throw new IllegalArgumentException("class is not fit");
        }
        BytesInfo info = CompatUtils.getDeclaredAnnotation(field, BytesInfo.class);
        int len = Array.getLength(fieldValue);
        if (len != info.len()){
            throw new IllegalArgumentException("the array len is not right");
        } else {

            Class<?> componentType = field.getType().getComponentType();
            int bytesUsed = 0;
            for (int i =0; i < len; i++){
                JavaDataType javaDataType = BytesParserUtils.getFitJavaDataType(componentType, info.unitLen(), info.sign());
                Object item = Array.get(fieldValue, i);
                ParsedResult result = javaDataType.parseToBytes(field, item, byteBuffer, beginPos);
                beginPos += result.getBytesUsed();
                bytesUsed += result.getBytesUsed();
            }
            return new ParsedResult(null, bytesUsed * 8);

        }

    }









    @Override
    ParsedResult parseToBean(Field field, ByteBuffer byteBuffer, int beginPos) {
        return null;
    }

    @Override
    ParsedResult parseToBytes(Field field, Object fieldValue, ByteBuffer byteBuffer, int beginPos) {
        return null;
    }
}
