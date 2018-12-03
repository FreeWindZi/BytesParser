package com.sht.bytesparser.bean;

import com.sht.bytesparser.annotation.BytesInfo;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class ReservedNode extends Node {

    int bytes = 0;
    ReservedNode(Field field) {
        super(field);
        BytesInfo annotation = field.getAnnotation(BytesInfo.class);
        bytes = annotation.len();
    }


    @Override
    public int evaluateSize(Object value) {
        return bytes;
    }

    @Override
    public void serialize(ByteBuffer buffer, Object value) {
        for (int i = 0; i < bytes; i++){
            buffer.put((byte) 0);
        }
    }

    @Override
    public Object deserialize(ByteBuffer buffer) {
        buffer.position(buffer.position() + bytes);
        return null;
    }
}
