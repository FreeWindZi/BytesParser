
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
public class StringNode extends FixOrNonFixNode {

    StringNode(Field field, Charset charset) {
        super(field, charset);
    }

    @Override
    public int evaluateSize(Object value) {
        if (isFix){
            return annotionLen;
        }else {
            return lenFlagBytesSize + valueToByteArray(value).length;
        }
    }

    @Override
    public void serialize(ByteBuffer buffer, Object value) {
        byte[] byteValue = valueToByteArray(value);
        if (byteValue == null){
            length = 0;
        }else {
            length = byteValue.length;
        }
        if (isFix){
            if (byteValue != null){
                // just for fixLength
                checkOverflow(byteValue);
                buffer.put(byteValue);
            }
            int remainSize = annotionLen - length;
            if (remainSize > 0) {
                // fill remains
                buffer.position(buffer.position() + remainSize);
            }
            return;
        } else {
            PrimitiveType.INT.serialize(length, buffer, lenFlagBytesSize, false);
            if (byteValue != null){
                buffer.put(byteValue);
            }
        }
    }

    @Override
    public Object deserialize(ByteBuffer buffer) {

        if (isFix){
            byte[] bytesValue = new byte[annotionLen];
            buffer.get(bytesValue);
            return new String(bytesValue, charset);
//            return deleteSuffix(new String(bytesValue, charset));

        } else {
            length = (int) PrimitiveType.INT.deserialize(buffer, lenFlagBytesSize, false);
            if (length == 0){
                return null;
            }else {
                byte[] bytesValue = new byte[length];
                buffer.get(bytesValue);
                return new String(bytesValue, charset);
            }
        }

    }

    private void checkOverflow(byte[] array) {
        if (array.length > annotionLen) {
            throw new IllegalValueException("Field [" + field.getName() + "] overflow, [bytes] should be larger");
        }
    }

    private byte[] valueToByteArray(Object value) {
        if (value == null){
            return null;
        }
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
