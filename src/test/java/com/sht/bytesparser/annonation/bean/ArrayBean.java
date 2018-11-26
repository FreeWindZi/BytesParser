package com.sht.bytesparser.annonation.bean;

import com.sht.bytesparser.annotation.BytesInfo;

import java.util.Arrays;

public class ArrayBean {

    public static class Template{
        @BytesInfo(order = 1)
        private byte result;
        @BytesInfo(order = 2, len = 10)
        private int[] tokenId;
        @BytesInfo(order = 4)
        private int res;

        public Template() {
        }

        public Template(byte result, int[] tokenId, int res) {
            this.result = result;
            this.tokenId = tokenId;
            this.res = res;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Template arrayBean = (Template) o;
            return result == arrayBean.result &&
                    res == arrayBean.res &&
                    Arrays.equals(tokenId, arrayBean.tokenId);
        }

        @Override
        public String toString() {
            return "ArrayBean.Template{" +
                    "result=" + result +
                    ", tokenId=" + Arrays.toString(tokenId) +
                    ", res=" + res +
                    '}';
        }
    }


}
