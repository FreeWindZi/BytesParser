package com.sht.bytesparser.parser.interf;

import com.sht.bytesparser.parser.ParsedResult;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * Created by navy on 2018/11/10.
 */

public interface ParsableType {


    ParsedResult parseBytesToBean(Object object, Field field, ByteBuffer byteBuffer, int beginPos);

    ParsedResult parseBeanToBytes(Field field, Object fieldValue, ByteBuffer byteBuffer, int beginPos);

    Object getNeedInitFieldValue(Field relateField, Object fieldValue, Field field);


    Object getRelateFieldValue(Object object, Field field);
}
