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

import com.sht.bytesparser.exception.InternalException;
import com.sht.bytesparser.util.AnnotationUtils;
import com.sht.bytesparser.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

/**
 * node for processing TairaData
 */
public class DataNode extends Node {

    /**
     * child field nodes
     */
    private List<Node> children = new LinkedList<>();

    /**
     * charset
     */
    private Charset charset;

    public DataNode(Class clazz, Charset charset) {
        super(clazz);
        this.charset = charset;
        generateChildren();
    }

    private DataNode(Field field, Charset charset) {
        super(field);
        this.charset = charset;
        generateChildren();
    }

    @Override
    public int evaluateSize(Object value) {
        int byteSize = 0;
        for (Node node : children) {
            byteSize += node.evaluateSize(ReflectionUtils.getFieldValue(value, node.field));
        }
        return byteSize;
    }

    @Override
    public void serialize(ByteBuffer buffer, Object value) {
        if (value == null) {
            buffer.position(buffer.position() + evaluateSize(null));
            return;
        }
        for (Node node : children) {
            node.serialize(buffer, ReflectionUtils.getFieldValue(value, node.field));
        }
    }

    @Override
    public Object deserialize(ByteBuffer buffer) {
        Object value = ReflectionUtils.createParamInstance(clazz);
        for (Node node : children) {
            ReflectionUtils.setField(value, node.field, node.deserialize(buffer));
        }
        return value;
    }

    private void generateChildren() {
        children.clear();
        List<Field> fields = AnnotationUtils.getSortedBytesInfo(clazz);
        for (Field field : fields) {
            PrimitiveType primitive = TypeConst.findPrimitive(field.getType());
            if (primitive != null) {
                children.add(new PrimitiveNode(field, primitive));
                continue;
            }
            if (TypeConst.isBytesSerializableClass(field.getType())) {
                children.add(new DataNode(field, charset));
                continue;
            }

            if (TypeConst.isSupportedCollection(field.getType()) || field.getType().isArray()) {
                children.add(new CollectionNode(field, charset));
                continue;
            }
            // other type appears, something is wrong with annotation check
            throw new InternalException(
                "Illegal field type [" + field.getType() + "] in class [" + clazz.getName() + "]");
        }
    }
}
