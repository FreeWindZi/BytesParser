package com.sht.bytesparser.bean;

import com.sht.bytesparser.annotation.BytesInfo;
import com.sht.bytesparser.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * node for processing fix or non-fix node; such as String, Array, Collection
 */
public abstract class FixOrNonFixNode extends Node {

    protected Charset charset;
    protected boolean isFix = true;
    protected int annotionLen = 0;
    protected int lenFlagBytesSize = 0;

    protected int length = 0;//String.length ; Array.size, Collection.size

    FixOrNonFixNode(Field field, Charset charset) {
        super(field);
        this.charset = charset;
        BytesInfo annotation = ReflectionUtils.getAnnotation(field, BytesInfo.class);
        annotionLen = annotation.len();
        lenFlagBytesSize = annotation.lenFlagBytesSize();
        if (annotionLen > 0){
            isFix = true;
        }else {
            isFix = false;
        }
        String charsetName = annotation.charsetName();
        if (!charsetName.equals("")){
            this.charset = Charset.forName(charsetName);
        }
    }
}
