///*
// * Copyright (c) 2018 Keep, Inc.
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in all
// * copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// * SOFTWARE.
// */
//package com.sht.bytesparser.bean;
//
//import com.gotokeep.keep.taira.annotation.ParamField;
//import com.gotokeep.keep.taira.exception.TairaIllegalValueException;
//import com.sht.bytesparser.annotation.BytesInfo;
//import com.sht.bytesparser.exception.IllegalValueException;
//import com.sht.bytesparser.util.ReflectionUtils;
//
//import java.lang.reflect.Field;
//import java.nio.ByteBuffer;
//import java.nio.charset.Charset;
//
///**
// * node for processing byte[] and String
// */
//public class StringNode extends Node {
//
//    private Charset charset;
//
//    private int bytes;
//
//    StringNode(Field field, Charset charset) {
//        super(field);
//        this.charset = charset;
//        BytesInfo annotation = ReflectionUtils.getAnnotation(field, BytesInfo.class);
//        bytes = annotation.;
//    }
//
//    @Override
//    public int evaluateSize(Object value) {
//        if (bytes <= 0 && value != null) {
//            return valueToByteArray(value).length;
//        }
//        return bytes;
//    }
//
//    @Override
//    public void serialize(ByteBuffer buffer, Object value) {
//        if (value == null) {
//            buffer.position(buffer.position() + bytes);
//            return;
//        }
//        byte[] byteValue = valueToByteArray(value);
//        if (bytes <= 0) {
//            // tail without [bytes]
//            buffer.put(byteValue);
//            return;
//        }
//        // with [bytes]
//        checkOverflow(byteValue);
//        int remainSize = bytes - byteValue.length;
//        buffer.put(byteValue);
//        if (remainSize > 0) {
//            // fill remains
//            buffer.position(buffer.position() + remainSize);
//        }
//    }
//
//    @Override
//    public Object deserialize(ByteBuffer buffer) {
//        byte[] bytesValue;
//        if (bytes <= 0) {
//            // tail byte array
//            bytesValue = new byte[buffer.remaining()];
//        } else {
//            bytesValue = new byte[bytes];
//        }
//        buffer.get(bytesValue);
//        if (String.class.equals(clazz)) {
//            return new String(bytesValue, charset);
//        } else {
//            return bytesValue;
//        }
//    }
//
//    private void checkOverflow(byte[] array) {
//        if (array.length > bytes) {
//            throw new IllegalValueException("Field [" + field.getName() + "] overflow, [bytes] should be larger");
//        }
//    }
//
//    private byte[] valueToByteArray(Object value) {
//        if (String.class.equals(clazz)) {
//            return ((String) value).getBytes(charset);
//        } else {
//            return (byte[]) value;
//        }
//    }
//}
