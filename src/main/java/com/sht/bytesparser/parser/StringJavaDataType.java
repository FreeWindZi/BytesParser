package com.sht.bytesparser.parser;

import com.sht.bytesparser.annotation.BytesInfo;
import com.sht.bytesparser.log.LoggerUtil;
import com.sht.bytesparser.util.BytesParserUtils;
import com.sht.bytesparser.util.CompatUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * Created by navy on 2018/11/13.
 */

public class StringJavaDataType extends JavaDataType {
    private String charsetName = "UTF-8";

    public StringJavaDataType() {
    }

    public StringJavaDataType(String charsetName) {
        this.charsetName = charsetName;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    public ParsedResult parseBytesToBean(Object object, Field field, ByteBuffer byteBuffer, int beginPos) {
        BytesInfo info = CompatUtils.getDeclaredAnnotation(field, BytesInfo.class);
        if (info == null) {
            throw new NullPointerException("String must use BytesInfo Annotation");
        }

        int dataLen = 0;
        if (info.len() > 0){
            dataLen = info.len();
        } else {
            Object relateValue = getRelateFieldValue(object, field);
            if (JavaDataType.BYTE_TYPE.contains(relateValue.getClass())){
                dataLen = (byte) relateValue;
            } else if (JavaDataType.SHORT_TYPE.contains(relateValue.getClass())){
                dataLen = (short) relateValue;
            } else if (JavaDataType.CHAR_TYPE.contains(relateValue.getClass())){
                dataLen = (char) relateValue;
            }else {
                dataLen = (int) relateValue;
            }
        }

        byte data[] = new byte[dataLen];
        byteBuffer.position(beginPos);
        byteBuffer.get(data, 0, data.length);
        String str = null;
        String charsetName = info.charsetName().equals("") ? this.charsetName : info.charsetName();
        try {
            str = new String(data, charsetName);
            if (info.len() > 0){
                int endStr = str.indexOf('\0') ;
                if (endStr != -1){
                    str = str.substring(0, endStr);
                }
            }
        } catch (UnsupportedEncodingException e) {
            LoggerUtil.e(e.toString());
        }
        return new ParsedResult(str, data.length * 8);


    }

    @Override
    public ParsedResult parseBeanToBytes(Field field, Object fieldValue, ByteBuffer byteBuffer, int beginPos) {
        BytesInfo info = CompatUtils.getDeclaredAnnotation(field, BytesInfo.class);
        if (info == null) {
            throw new NullPointerException("String must use BytesInfo Annotation");
        }
        byteBuffer.position(beginPos);
        String charsetName = info.charsetName().equals("") ? this.charsetName : info.charsetName();
        byte temp[] = null;
        try {
             temp = ((String)fieldValue).getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("charsetName is not support");
        }

        if (info.len() > 0){ //处理定长
            if (info.len() >= temp.length){
                byteBuffer.put(temp);
            }else {
                LoggerUtil.e("字符串过长");
                throw new IllegalArgumentException("the length of string bytes is too big, info.len = "+ info.len()
                        +", but the bytes length is " + temp.length);
            }
            //byteBuffer.position(byteBuffer.position() + info.len());
            return new ParsedResult(null, info.len() * 8);
        } else {
            byteBuffer.put(temp);
            return new ParsedResult(null, temp.length * 8);
        }


    }



    @Override
    public Object getNeedInitFieldValue(Field relateField, Object fieldValue, Field field){
        BytesInfo info = CompatUtils.getDeclaredAnnotation(field, BytesInfo.class);
        if (info == null) {
            throw new NullPointerException("String must use BytesInfo Annotation");
        }

        if (fieldValue == null){
            return 0;
        }

        String charsetName = info.charsetName().equals("") ? this.charsetName : info.charsetName();
        try {
            byte temp[] = ((String)fieldValue).getBytes(charsetName);
            return temp.length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("charsetName is not support");
        }
    }





    public Object getRelateFieldValue(Object object, Field field){
        String relateFieldName = field.getName() + BytesInfo.LEN_FLAG_SUFFIX;
        Field relateField = null;
        try {
            relateField = object.getClass().getDeclaredField(relateFieldName);
            BytesInfo relateBytesInfo = CompatUtils.getDeclaredAnnotation(field, BytesInfo.class);
            if (relateBytesInfo == null || !relateBytesInfo.lenFlag()){
                throw new NullPointerException("Field named " + relateFieldName +", has not relate flag, lenFlag must be true");
            }
            Object relateValue = BytesParserUtils.getFieldValue(object, relateField);
            return relateValue;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("no such Field, " +  relateFieldName);
        }

    }

    //not need
    @Override
    ParsedResult parseToBean(Field field, ByteBuffer byteBuffer, int beginPos) {
        return null;
    }

    @Override
    ParsedResult parseToBytes(Field field, Object fieldValue, ByteBuffer byteBuffer, int beginPos) {
        return null;
    }
}