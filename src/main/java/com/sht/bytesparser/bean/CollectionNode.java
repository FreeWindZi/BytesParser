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
import com.sht.bytesparser.exception.InternalException;
import com.sht.bytesparser.util.ReflectionUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

/**
 * node for processing collection & array
 */
@SuppressWarnings({ "unchecked" })
public class CollectionNode extends FixOrNonFixNode {

    /**
     * member type
     */
    private Class memberType;

    /**
     * member type node
     */
    private Node memberNode;

    CollectionNode(Field field, Charset charset) {
        super(field, charset);
        memberType = ReflectionUtils.getCollectionFirstMemberType(field);
        memberNode = createMemberNode();
    }

    @Override
    public int evaluateSize(Object value) {
        if (isFix){
            length = annotionLen;
        } else {
            length = getCollectionLength(value);
        }

        if (value == null){
            if (isFix){
                return length * memberNode.evaluateSize(null);
            }else {
                return lenFlagBytesSize;
            }
        }



        int byteSize = 0;
        if (! isFix){
            byteSize += lenFlagBytesSize;
        }

        if (value.getClass().isArray()){
            for (int i = 0; i < length; i++){
                byteSize += memberNode.evaluateSize(Array.get(value, i));
            }
        } else {
            Collection collection = (Collection) value;
            for (Object member : collection) {
                byteSize += memberNode.evaluateSize(member);
            }
        }
        return byteSize;
    }

    @Override
    public void serialize(ByteBuffer buffer, Object value) {
        length = getCollectionLength(value);
        if (isFix){
            checkOverflow(length);
            serializeMembers(buffer, value);
        }else {
            PrimitiveType.INT.serialize(length, buffer, lenFlagBytesSize, false);
            serializeMembers(buffer, value);
        }
    }

    @Override
    public Object deserialize(ByteBuffer buffer) {

        Object fieldValue = null;
        int length = 0;
        if (isFix){
            length = annotionLen;
        } else {
            length = (int) PrimitiveType.INT.deserialize(buffer, lenFlagBytesSize, false);
        }
        boolean isArray = false;
        if (clazz.isArray()){
            fieldValue = Array.newInstance(memberType, length);
            isArray = true;
        } else {
            fieldValue = TypeConst.newCollection(clazz);
        }

        for (int i = 0; i < length; i++){
            if (isArray){
                Array.set(fieldValue, i, memberNode.deserialize(buffer));
            }else {
                ((Collection) fieldValue).add(memberNode.deserialize(buffer));
            }
        }

        return fieldValue;
    }

    private Node createMemberNode() {
        PrimitiveType primitive = TypeConst.findPrimitive(memberType);
        if (primitive != null) {
            return new PrimitiveNode(memberType, primitive);
        } else if (TypeConst.isBytesSerializableClass(memberType)) {
            return new DataNode(memberType, charset);
        } else {
            // illegal type, annotation error
            throw new InternalException(
                "Illegal field type [" + field.getType() + "] in class [" + clazz.getName() + "]");
        }
    }

    private int getCollectionLength(Object value) {
        if (value != null) {
            if (clazz.isArray()) {
                return Array.getLength(value);
            } else if (TypeConst.isSupportedCollection(clazz)) {
                return ((Collection) value).size();
            }
        }
        return 0;
    }

    private void serializeMembers(ByteBuffer buffer, Object value) {
        length = getCollectionLength(value);
        if (clazz.isArray()) {
            for (int i = 0; i < length; i++) {
                memberNode.serialize(buffer, Array.get(value, i));
            }
        } else {
            Collection collection = (Collection) value;
            for (Object member : collection) {
                memberNode.serialize(buffer, member);
            }
        }
        // node with length, fill remain empty bytes
        if (isFix) {
            int memberByteSize = memberNode.evaluateSize(null);
            buffer.position(buffer.position() + (annotionLen - length) * memberByteSize);
        }
    }

    private void checkOverflow(int memberCount) {
        if (memberCount > annotionLen) {
            throw new IllegalValueException("Field [" + field.getName() + "] overflow, [length] should be larger");
        }
    }
}
