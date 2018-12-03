package com.sht.bytesparser.annonation.bean;

import com.sht.bytesparser.annotation.BytesInfo;
import com.sht.bytesparser.annotation.BytesSerializable;

import java.util.Objects;

public class ContainerBean {

    public static class Father implements BytesSerializable {
        @BytesInfo(order = 0)
        public int fInt;

    }
    public static class Son extends Father  {
        @BytesInfo(order = 1, len = 1, sign = true)
        public int sonInt;
        @BytesInfo(order = 2)
        public float sonf;
        @BytesInfo(order = 3)
        public char sonChar;
        @Override
        public String toString() {
            return "Son{" +
                    "fInt=" + fInt +
                    ", sonValue=" + sonInt +
                    ", sonf=" + sonf +
                    ", sonChar=" + sonChar +
                    '}';
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Son son = (Son) o;
            return fInt == son.fInt &&
                    sonInt == son.sonInt &&
                    Float.compare(son.sonf, sonf) == 0 &&
                    sonChar == son.sonChar;
        }

    }
}
