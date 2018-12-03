package com.sht.bytesparser.annonation.bean;

import com.sht.bytesparser.annotation.BytesInfo;
import com.sht.bytesparser.annotation.BytesSerializable;
import com.sht.bytesparser.bean.Reserved;

import java.util.Objects;

public class ReservedBean implements BytesSerializable {

    @BytesInfo(order = 1, len = 2)
    private int temperature;
    @BytesInfo(order = 1, len = 10)
    private Reserved reserved;
    @BytesInfo(order = 2)
    private long time;

    public ReservedBean() {
    }

    public ReservedBean(int temperature, Reserved reserved, long time) {
        this.temperature = temperature;
        this.reserved = reserved;
        this.time = time;
    }

    @Override
    public String toString() {
        return "ReservedBean{" +
                "temperature=" + temperature +
                ", reserved=" + reserved +
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservedBean that = (ReservedBean) o;
        return temperature == that.temperature &&
                time == that.time &&
                Objects.equals(reserved, that.reserved);
    }

    public Reserved getReserved() {
        return reserved;
    }
}
