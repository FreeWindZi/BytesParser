package com.sht.bytesparser.annonation.bean;

import com.sht.bytesparser.annotation.BytesInfo;
import com.sht.bytesparser.annotation.BytesSerializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ArrayBean {

    public static class Template implements BytesSerializable{
        @BytesInfo(order = 0)
        private byte result;
        @BytesInfo(order = 1, len = 10)
        private int[] tokenId;
        @BytesInfo(order = 2)
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



    public static class Student implements BytesSerializable{
//        @BytesInfo(order = 0, lenFlagBytesSize = 2)
//        public String name;
        @BytesInfo(order = 0)
        public int id;
        @BytesInfo(order = 1)
        public int grade;
        @BytesInfo(order = 2, lenFlagBytesSize = 1)
        public List<Byte> books = new ArrayList<>();


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Student student = (Student) o;
            return id == student.id &&
                    grade == student.grade &&
                    Objects.equals(books, student.books);
        }


        @Override
        public String toString() {
            return "Student{" +
//                    "name='" + name + '\'' +
                    ", id=" + id +
                    ", books=" + books +
                    ", grade=" + grade +
                    '}';
        }
    }

    public static class Teacher implements BytesSerializable{
        @BytesInfo(order = 0, lenFlagBytesSize = 2)
        public String name;
        @BytesInfo(order = 1, lenFlagBytesSize = 2)
        public List<Student> students = new ArrayList<>();
        @BytesInfo(order = 2)
        public int id;

        public Teacher() {
        }

        @Override
        public String toString() {
            return "Teacher{" +
                    "name='" + name + '\'' +
                    ", students=" + students +
                    ", id=" + id +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Teacher teacher = (Teacher) o;
            return id == teacher.id &&
                    Objects.equals(name, teacher.name) &&
                    Objects.equals(students, teacher.students);
        }


    }
}
