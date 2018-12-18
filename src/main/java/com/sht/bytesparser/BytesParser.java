package com.sht.bytesparser;

import com.sht.bytesparser.annotation.BytesSerializable;
import com.sht.bytesparser.bean.ClassInfo;
import com.sht.bytesparser.bean.DataNode;
import com.sht.bytesparser.exception.AnnotationException;
import com.sht.bytesparser.exception.IllegalValueException;
import com.sht.bytesparser.exception.InternalException;
import com.sht.bytesparser.log.LoggerUtil;
import com.sht.bytesparser.util.AnnotationUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BytesParser {
    private ByteOrder order = ByteOrder.BIG_ENDIAN;
    private Charset charset = Charset.forName("UTF-8");
    private BytesParser() {
    }
    private BytesParser(BytesParser origin) {
        this.order = origin.order;
        this.charset = origin.charset;
    }

    public static class Builder {

        private BytesParser target;

        public Builder() {
            target = new BytesParser();
        }

        public Builder order(ByteOrder order) {
            target.order = order;
            return this;
        }

        public Builder charsetName(Charset charset) {
            target.charset = charset;
            return this;
        }

        public BytesParser build() {
            return new BytesParser(target);
        }

    }


    private final Map<Class<? extends BytesSerializable>, DataNode> rootNodeCache = new HashMap<>();
    private final Map<Class<? extends BytesSerializable>, ClassInfo> rootNodeClassInfoCache = new HashMap<>();




    public <T extends BytesSerializable> byte[] toBytes(T object) throws AnnotationException, IllegalValueException {
        if (object == null) {
            return null;
        }
        try {
            AnnotationUtils.checkAnnotationOrThrow(object.getClass());
            DataNode root = getTairaNode(object.getClass(), charset);
            int byteSize = root.evaluateSize(object);
            ByteBuffer byteBuffer = ByteBuffer.allocate(byteSize).order(order);
            root.serialize(byteBuffer, object);
            return byteBuffer.array();
        } catch (InternalException e) {
            LoggerUtil.e(e.toString());
        }
        return null;
    }
    public <T extends BytesSerializable> void toBytes(T object, byte data[], int start) throws AnnotationException, IllegalValueException{
        if (data == null || data.length <= start){
            throw new InternalException("data[] length is small");
        }
        byte src[] = toBytes(object);
        if (src != null){
            System.arraycopy(src, 0, data, start, src.length);
        }
    }



    public  <T extends BytesSerializable> T toBean( Class<T> clazz, byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        ByteBuffer buffer;
        try {
            AnnotationUtils.checkAnnotationOrThrow(clazz);
            buffer = ByteBuffer.wrap(data).order(order);
        } catch (IndexOutOfBoundsException | IllegalStateException e) {
            throw new InternalException(e);
        }
        return deserializeBuffer(buffer, clazz);
    }

    public  <T extends BytesSerializable> T toBean( Class<T> clazz, byte[] data, int start) {
        if (data == null || data.length <= start){
            throw new InternalException("data[] length is small");
        }
        byte dst[] = new byte[data.length - start];
        System.arraycopy(data, start, dst, 0, dst.length);
        return toBean(clazz, dst);

    }

    private <T extends BytesSerializable> T deserializeBuffer(ByteBuffer buffer, Class<T> clazz) {
        DataNode root = getTairaNode(clazz, charset);
        return (T) root.deserialize(buffer);
    }


    private DataNode getTairaNode(Class<? extends BytesSerializable> clazz, Charset charset) {
        DataNode node = rootNodeCache.get(clazz);
        if (node == null) {
            node = new DataNode(clazz, charset);
            rootNodeCache.put(clazz, node);
        }
        return node;
    }

    private ClassInfo getClassInfo(Class<? extends BytesSerializable> clazz){
        ClassInfo classInfo = rootNodeClassInfoCache.get(clazz);
        if (classInfo == null){
            classInfo = new ClassInfo();
//            classInfo.nonFixLength = AnnotationUtils.isNonFixLength(clazz);
//            classInfo.byteSize = AnnotationUtils.getMinByteSize(clazz);
        }
        return classInfo;
    }




}
