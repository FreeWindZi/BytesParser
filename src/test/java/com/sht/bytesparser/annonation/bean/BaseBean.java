package com.sht.bytesparser.annonation.bean;


import com.sht.bytesparser.annotation.BytesInfo;
import com.sht.bytesparser.annotation.BytesSerializable;

import java.util.Objects;

public  class BaseBean {


    public static class Template implements BytesSerializable {
        @BytesInfo(order = 0)
        byte byteValue;
        @BytesInfo(order = 1)
        short shortValue;
        @BytesInfo(order = 2)
        char charValue;
        @BytesInfo(order = 3)
        int intValue;
        @BytesInfo(order = 4)
        long longValue;
        @BytesInfo(order = 5)
        float floatValue;
        @BytesInfo(order = 6)
        double doubleValue;

        public Template() {
        }

        public Template(byte byteValue, short shortValue, char charValue, int intValue, long longValue, float floatValue, double doubleValue) {
            this.byteValue = byteValue;
            this.shortValue = shortValue;
            this.charValue = charValue;
            this.intValue = intValue;
            this.longValue = longValue;
            this.floatValue = floatValue;
            this.doubleValue = doubleValue;
        }

        public byte getByteValue() {
            return byteValue;
        }

        public void setByteValue(byte byteValue) {
            this.byteValue = byteValue;
        }

        public short getShortValue() {
            return shortValue;
        }

        public void setShortValue(short shortValue) {
            this.shortValue = shortValue;
        }

        public char getCharValue() {
            return charValue;
        }

        public void setCharValue(char charValue) {
            this.charValue = charValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }

        public long getLongValue() {
            return longValue;
        }

        public void setLongValue(long longValue) {
            this.longValue = longValue;
        }

        public float getFloatValue() {
            return floatValue;
        }

        public void setFloatValue(float floatValue) {
            this.floatValue = floatValue;
        }

        public double getDoubleValue() {
            return doubleValue;
        }

        public void setDoubleValue(double doubleValue) {
            this.doubleValue = doubleValue;
        }

        @Override
        public String toString() {
            return "BaseBean.Template{" +
                    "byteValue=" + byteValue +
                    ", shortValue=" + shortValue +
                    ", charValue=" + charValue +
                    ", intValue=" + intValue +
                    ", longValue=" + longValue +
                    ", floatValue=" + floatValue +
                    ", doubleValue=" + doubleValue +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Template template = (Template) o;
            return byteValue == template.byteValue &&
                    shortValue == template.shortValue &&
                    charValue == template.charValue &&
                    intValue == template.intValue &&
                    longValue == template.longValue &&
                    Float.compare(template.floatValue, floatValue) == 0 &&
                    Double.compare(template.doubleValue, doubleValue) == 0;
        }

    }


    public static class Template2 implements BytesSerializable{
        @BytesInfo(order = 1)
        Byte byteValue;
        @BytesInfo(order = 2)
        Short shortValue;
        @BytesInfo(order = 3)
        Character charValue;
        @BytesInfo(order = 4)
        Integer intValue;
        @BytesInfo(order = 5)
        Long longValue;
        @BytesInfo(order = 6)
        Float floatValue;
        @BytesInfo(order = 7)
        Double doubleValue;

        public Template2() {
        }

        public Template2(Byte byteValue, Short shortValue, Character charValue, Integer intValue, Long longValue, Float floatValue, Double doubleValue) {
            this.byteValue = byteValue;
            this.shortValue = shortValue;
            this.charValue = charValue;
            this.intValue = intValue;
            this.longValue = longValue;
            this.floatValue = floatValue;
            this.doubleValue = doubleValue;
        }

        public Byte getByteValue() {
            return byteValue;
        }

        public void setByteValue(Byte byteValue) {
            this.byteValue = byteValue;
        }

        public Short getShortValue() {
            return shortValue;
        }

        public void setShortValue(Short shortValue) {
            this.shortValue = shortValue;
        }

        public Character getCharValue() {
            return charValue;
        }

        public void setCharValue(Character charValue) {
            this.charValue = charValue;
        }

        public Integer getIntValue() {
            return intValue;
        }

        public void setIntValue(Integer intValue) {
            this.intValue = intValue;
        }

        public Long getLongValue() {
            return longValue;
        }

        public void setLongValue(Long longValue) {
            this.longValue = longValue;
        }

        public Float getFloatValue() {
            return floatValue;
        }

        public void setFloatValue(Float floatValue) {
            this.floatValue = floatValue;
        }

        public Double getDoubleValue() {
            return doubleValue;
        }

        public void setDoubleValue(Double doubleValue) {
            this.doubleValue = doubleValue;
        }


        @Override
        public String toString() {
            return "Template2{" +
                    "byteValue=" + byteValue +
                    ", shortValue=" + shortValue +
                    ", charValue=" + charValue +
                    ", intValue=" + intValue +
                    ", longValue=" + longValue +
                    ", floatValue=" + floatValue +
                    ", doubleValue=" + doubleValue +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Template2 template2 = (Template2) o;
            return Objects.equals(byteValue, template2.byteValue) &&
                    Objects.equals(shortValue, template2.shortValue) &&
                    Objects.equals(charValue, template2.charValue) &&
                    Objects.equals(intValue, template2.intValue) &&
                    Objects.equals(longValue, template2.longValue) &&
                    Objects.equals(floatValue, template2.floatValue) &&
                    Objects.equals(doubleValue, template2.doubleValue);
        }


    }
}