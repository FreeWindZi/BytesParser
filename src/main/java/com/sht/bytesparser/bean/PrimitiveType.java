package com.sht.bytesparser.bean;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public enum  PrimitiveType implements PrimitiveProcessor {
    BOOLEAN {
        private static final byte TRUE = 1;
        private static final byte FALSE = 0;

        @Override
        public void serialize(Object value, ByteBuffer buffer, int size) {
            buffer.put((Boolean) value ? TRUE : FALSE);
        }

        @Override
        public Object deserialize(ByteBuffer buffer, int size) {
            return buffer.get() == TRUE;
        }

        @Override
        public boolean canProcess(Class clazz) {
            return clazz == boolean.class || clazz == Boolean.class;
        }

        @Override
        public int byteSize() {
            return 1;
        }

        @Override
        public Object defaultValue() {
            return false;
        }
    },

    BYTE {
        @Override
        public void serialize(Object value, ByteBuffer buffer, int size) {
            buffer.put((byte) value);
        }

        @Override
        public Object deserialize(ByteBuffer buffer, int size) {
            return buffer.get();
        }

        @Override
        public boolean canProcess(Class clazz) {
            return clazz == byte.class || clazz == Byte.class;
        }

        @Override
        public int byteSize() {
            return 1;
        }

        @Override
        public Object defaultValue() {
            return 0;
        }
    },

    CHAR {
        @Override
        public void serialize(Object value, ByteBuffer buffer, int size) {
            PrimitiveType.putLowerBytes(buffer, (char) value, size);
        }

        @Override
        public Object deserialize(ByteBuffer buffer, int size) {
            long longValue = PrimitiveType.getBytesToLong(buffer, size);
            return (char) longValue;
        }

        @Override
        public boolean canProcess(Class clazz) {
            return clazz == char.class || clazz == Character.class;
        }

        @Override
        public int byteSize() {
            return 2;
        }

        @Override
        public Object defaultValue() {
            return '\u0000';
        }
    },

    SHORT {
        @Override
        public void serialize(Object value, ByteBuffer buffer, int size) {
            PrimitiveType.putLowerBytes(buffer, (short) value, size);
        }

        @Override
        public Object deserialize(ByteBuffer buffer, int size) {
            return (short) PrimitiveType.getBytesToLong(buffer, size);
        }

        @Override
        public boolean canProcess(Class clazz) {
            return clazz == short.class || clazz == Short.class;
        }

        @Override
        public int byteSize() {
            return 2;
        }

        @Override
        public Object defaultValue() {
            return 0;
        }
    },

    INT {
        @Override
        public void serialize(Object value, ByteBuffer buffer, int size) {
            PrimitiveType.putLowerBytes(buffer, (int) value, size);
        }

        @Override
        public Object deserialize(ByteBuffer buffer, int size) {
            return (int) PrimitiveType.getBytesToLong(buffer, size);
        }

        @Override
        public boolean canProcess(Class clazz) {
            return clazz == int.class || clazz == Integer.class;
        }

        @Override
        public int byteSize() {
            return 4;
        }

        @Override
        public Object defaultValue() {
            return 0;
        }
    },

    FLOAT {
        @Override
        public void serialize(Object value, ByteBuffer buffer, int size) {
            PrimitiveType.putLowerBytes(buffer, Float.floatToRawIntBits((Float) value), size);
        }

        @Override
        public Object deserialize(ByteBuffer buffer, int size) {
            long longVal = PrimitiveType.getBytesToLong(buffer, size);
            return Float.intBitsToFloat((int) longVal);
        }

        @Override
        public boolean canProcess(Class clazz) {
            return clazz == float.class || clazz == Float.class;
        }

        @Override
        public int byteSize() {
            return 4;
        }

        @Override
        public Object defaultValue() {
            return 0f;
        }
    },

    DOUBLE {
        @Override
        public void serialize(Object value, ByteBuffer buffer, int size) {
            PrimitiveType.putLowerBytes(buffer, Double.doubleToRawLongBits((Double) value), size);
        }

        @Override
        public Object deserialize(ByteBuffer buffer, int size) {
            long longVal = PrimitiveType.getBytesToLong(buffer, size);
            return Double.longBitsToDouble(longVal);
        }

        @Override
        public boolean canProcess(Class clazz) {
            return clazz == double.class || clazz == Double.class;
        }

        @Override
        public int byteSize() {
            return 8;
        }

        @Override
        public Object defaultValue() {
            return 0d;
        }
    },

    LONG {
        @Override
        public void serialize(Object value, ByteBuffer buffer, int size) {
            PrimitiveType.putLowerBytes(buffer, (Long) value, size);
        }

        @Override
        public Object deserialize(ByteBuffer buffer, int size) {
            return PrimitiveType.getBytesToLong(buffer, size);
        }

        @Override
        public boolean canProcess(Class clazz) {
            return clazz == long.class || clazz == Long.class;
        }

        @Override
        public int byteSize() {
            return 8;
        }

        @Override
        public Object defaultValue() {
            return 0L;
        }
    };


    /**
     * put long value lower bytes into buffer
     *
     * @param buffer buffer
     * @param longVal long value
     * @param size lower bytes count
     */
    private static void putLowerBytes(ByteBuffer buffer, long longVal, int size) {
        for (int i = size; i > 0; i--) {
            int shift;
            if (buffer.order() == ByteOrder.LITTLE_ENDIAN) {
                shift = size - i;
            } else {
                shift = i - 1;
            }
            byte lastByte = (byte) ((longVal >>> (8 * shift)) & 0xFF);
            buffer.put(lastByte);
        }
    }

    /**
     * get bytes from buffer and cast to long value
     *
     * @param buffer buffer
     * @param size byte size
     * @return long value
     */
    private static long getBytesToLong(ByteBuffer buffer, int size) {
        long longValue = 0;
        for (int i = 0; i < size; i++) {
            byte byteValue = buffer.get();
            if (buffer.order() == ByteOrder.LITTLE_ENDIAN) {
                longValue += ((long) byteValue & 0xffL) << (8 * i);
            } else {
                longValue = (longValue << 8) + (byteValue & 0xff);
            }
        }


        return longValue;
    }


}
