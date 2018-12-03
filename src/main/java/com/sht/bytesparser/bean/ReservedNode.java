package com.sht.bytesparser.bean;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class ReservedNode extends Node {


//    ReservedNode(Field field, int ) {
//        super(field);
//    }

    ReservedNode(Field field) {
        super(field);
    }

    @Override
    public int evaluateSize(Object value) {
        return 0;
    }

    @Override
    public void serialize(ByteBuffer buffer, Object value) {

    }

    @Override
    public Object deserialize(ByteBuffer buffer) {
        return null;
    }
}
