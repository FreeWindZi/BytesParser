/*
 * Copyright (c) 2018 Keep, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.sht.bytesparser.bean;


import com.sht.bytesparser.annotation.BytesInfo;
import com.sht.bytesparser.exception.IllegalValueException;
import com.sht.bytesparser.util.AnnotationUtils;
import com.sht.bytesparser.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * node for processing primitive type
 */
public class PrimitiveNode extends Node {

    private PrimitiveType type;

    private int bytes;
    private boolean sign = true;

    /**
     * collection member node without field
     */
    public PrimitiveNode(Class clazz, PrimitiveType type) {
        super(clazz);
        this.type = type;
        bytes = type.byteSize();
    }

    /**
     * field node
     */
    public PrimitiveNode(Field field, PrimitiveType type) {
        super(field);
        this.field = field;
        this.type = type;
        BytesInfo info = ReflectionUtils.getAnnotation(field, BytesInfo.class);
        bytes = AnnotationUtils.evaluatePrimitiveSize(type, info.len());
        this.sign = info.sign();
    }

    @Override
    public int evaluateSize(Object value) {
        return bytes;
    }

    @Override
    public void serialize(ByteBuffer buffer, Object value) {
        checkOverflow(value);
        type.serialize(value == null ? type.defaultValue() : value, buffer, bytes);
    }

    @Override
    public Object deserialize(ByteBuffer buffer) {
        return type.deserialize(buffer, bytes);
    }



    private void checkOverflow(Object value) throws IllegalValueException {
        if (type.byteSize() <= 1 || value == null) {
            return;
        }
        // check value overflow when [bytes] specified
        double doubleValue;
        if (PrimitiveType.CHAR.canProcess(clazz)) {
            // char to number
            doubleValue = Character.getNumericValue((Character) value);
        } else {
            doubleValue = ((Number) value).doubleValue();
        }

        if (sign){
            double max = Math.pow(2, 8 * bytes - 1);
            if (doubleValue >= -max && doubleValue < max) {
                return;
            }
        }else {
            double max = Math.pow(2, 8 * bytes);
            if (doubleValue < max) {
                return;
            }
        }


        throw new IllegalValueException("Value [" + value + "] overflow, [bytes] should be larger");
    }
}
