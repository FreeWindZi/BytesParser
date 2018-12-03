
package com.sht.bytesparser.bean;


import com.sht.bytesparser.annotation.BytesInfo;
import com.sht.bytesparser.exception.IllegalValueException;
import com.sht.bytesparser.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * node for processing String
 */
public class StringNode extends Node {

    private Charset charset;

    private int bytes;

    StringNode(Field field, Charset charset) {
        super(field);
        this.charset = charset;
        BytesInfo annotation = ReflectionUtils.getAnnotation(field, BytesInfo.class);
        bytes = annotation.len();
    }

    @Override
    public int evaluateSize(Object value) {
        if (bytes <= 0 && value != null) {
            return valueToByteArray(value).length;
        }
        return bytes;
    }

    @Override
    public void serialize(ByteBuffer buffer, Object value) {
        if (value == null) {
            buffer.position(buffer.position() + bytes);
            return;
        }
        byte[] byteValue = valueToByteArray(value);
        if (bytes <= 0) {
            // tail without [bytes]
            buffer.put(byteValue);
            return;
        }
        // with [bytes]
        checkOverflow(byteValue);
        int remainSize = bytes - byteValue.length;
        buffer.put(byteValue);
        if (remainSize > 0) {
            // fill remains
            buffer.position(buffer.position() + remainSize);
        }
    }

    @Override
    public Object deserialize(ByteBuffer buffer) {
        byte[] bytesValue;
        if (bytes <= 0) {
            bytesValue = new byte[buffer.remaining()];
        } else {
            bytesValue = new byte[bytes];
        }
        buffer.get(bytesValue);
        return deleteSuffix(new String(bytesValue, charset));
    }

    private void checkOverflow(byte[] array) {
        if (array.length > bytes) {
            throw new IllegalValueException("Field [" + field.getName() + "] overflow, [bytes] should be larger");
        }
    }

    private byte[] valueToByteArray(Object value) {
        return ((String) value).getBytes(charset);
    }

    private String deleteSuffix(String str){
        int endStr = str.indexOf('\0') ;
        if (endStr != -1){
            str = str.substring(0, endStr);
        }
        return str;
    }
}
