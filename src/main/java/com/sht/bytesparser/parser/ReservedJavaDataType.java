package com.sht.bytesparser.parser;

import com.sht.bytesparser.annotation.BytesInfo;
import com.sht.bytesparser.util.CompatUtils;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * Created by navy on 2018/11/15.
 */

public class ReservedJavaDataType extends JavaDataType {

    @Override
    public ParsedResult parseBytesToBean(Object object, Field field, ByteBuffer byteBuffer, int beginPos) {
        BytesInfo info = CompatUtils.getDeclaredAnnotation(field, BytesInfo.class);
        if (info == null) {
            throw new NullPointerException("Reserved must use BytesInfo Annotation");
        }


        return new ParsedResult( null, info.len() * 8);
    }

    @Override
    public ParsedResult parseBeanToBytes(Field field, Object fieldValue, ByteBuffer byteBuffer, int beginPos) {
        BytesInfo info = CompatUtils.getDeclaredAnnotation(field, BytesInfo.class);
        if (info == null) {
            throw new NullPointerException("Reserved must use BytesInfo Annotation");
        }
        return new ParsedResult( null, info.len() * 8);
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
