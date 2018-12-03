package com.sht.bytesparser.annonation.bean;

import com.sht.bytesparser.annotation.BytesInfo;
import com.sht.bytesparser.annotation.BytesSerializable;

import java.util.Objects;

public class StringBean {

    public static class Template implements BytesSerializable {
        @BytesInfo(order = 0, len = 20)
        String accout;
        @BytesInfo(order = 1, len = 16)
        String password;

        public Template() {
        }

        public Template(String accout, String password) {
            this.accout = accout;
            this.password = password;
        }

        @Override
        public String toString() {
            return "Template{" +
                    "accout='" + accout + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Template template = (Template) o;
            return Objects.equals(accout, template.accout) &&
                    Objects.equals(password, template.password);
        }

    }


    public static class Template2 implements BytesSerializable{

        @BytesInfo(order = 0, lenFlagBytesSize = 2)
        String name;
        @BytesInfo(order = 1, lenFlagBytesSize = 2)
        String headerUrl;

        public Template2() {
        }

        public Template2( String name, String headerUrl) {
            this.name = name;
            this.headerUrl = headerUrl;
        }

        @Override
        public String toString() {
            return "Template2{" +
                    ", name='" + name + '\'' +
                    ", headerUrl='" + headerUrl + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Template2 template2 = (Template2) o;
            return Objects.equals(name, template2.name) &&
                    Objects.equals(headerUrl, template2.headerUrl);
        }

    }

}
