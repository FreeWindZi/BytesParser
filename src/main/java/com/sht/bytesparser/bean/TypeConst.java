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

import com.sht.bytesparser.annotation.BytesSerializable;
import com.sht.bytesparser.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * define some BytesSerializable supported types and functions
 */
public final class TypeConst {

    /**
     * BytesSerializable instance type
     */
    private static final Class<BytesSerializable> BYTES_SERIALIZABLE_CLASS = BytesSerializable.class;

    /**
     * currently BytesParser supports List & Set
     */
    private static final Set<Class<? extends Collection>> SUPPORTED_COLLECTION_TYPE = new HashSet<>();

    static {
        SUPPORTED_COLLECTION_TYPE.add(List.class);
        SUPPORTED_COLLECTION_TYPE.add(Set.class);
    }

    private TypeConst() {}

    /**
     * get a primitive type processor
     *
     * @param clazz class
     * @return primitive type
     */
    public static PrimitiveType findPrimitive(Class clazz) {
        for (PrimitiveType type : PrimitiveType.values()) {
            if (type.canProcess(clazz)) {
                return type;
            }
        }
        return null;
    }

    /**
     * whether it's a supported collection
     *
     * @param fieldType class
     * @return true if supported
     */
    public static boolean isSupportedCollection(Class fieldType) {
        for (Class clazz : TypeConst.SUPPORTED_COLLECTION_TYPE) {
            if (clazz.equals(fieldType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * create collection instance when deserialize
     *
     * @return collection instance
     */
    public static Collection newCollection(Class fieldType) {
        if (List.class.equals(fieldType)) {
            return new ArrayList();
        } else if (Set.class.equals(fieldType)) {
            return new HashSet();
        }
        return null;
    }

    /**
     * whether it's BytesSerializable instance
     *
     * @param clazz class
     * @return true if it's BytesSerializable
     */
    public static boolean isBytesSerializableClass(Class clazz) {
        return clazz != null && ReflectionUtils.isParentClass(BYTES_SERIALIZABLE_CLASS, clazz);
    }


}
