package com.sht.bytesparser.annonation;

import com.sht.bytesparser.BytesParser;
import com.sht.bytesparser.annotation.BitsInfo;
import com.sht.bytesparser.annotation.BytesInfo;
import com.sht.bytesparser.log.LoggerUtil;
import com.sht.bytesparser.parser.JavaDataType;
import com.sht.bytesparser.parser.StringJavaDataType;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Random;

/**
 * Created by navy on 2018/11/13.
 */

public class BaseTypeTest {

    public static class Student {
        @BytesInfo(order = 1, len = 2, sign = true)
        int id;
        @BytesInfo(order = 2, len = 4)
        double grade;
        @BytesInfo(order = 3)
        byte gander;
        @BytesInfo(order = 4, len = 10)
        String name;
        @BytesInfo(order = 6, len = 20)
        String accout;
        @BytesInfo(order = 7 , len = 8)
        Long number;
        @BytesInfo(order = 8 , len = 2, sign = false)
        private int lesson;


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Student student = (Student) o;

            if (id != student.id) return false;
            if (Double.compare(student.grade, grade) != 0) return false;
            if (gander != student.gander) return false;
            if (!number.equals(student.number)) return false;
            if (lesson != student.lesson) return false;
            if (!name.equals(student.name)) return false;
            return accout.equals(student.accout);
        }

        @Override
        public String toString() {
            return "Student{" +
                    "id=" + id +
                    ", grade=" + grade +
                    ", gander=" + gander +
                    ", name='" + name + '\'' +
                    ", accout='" + accout + '\'' +
                    ", number=" + number +
                    ", lesson=" + lesson +
                    '}';
        }
    }

    @Test
    public void testFixedlengthType() {
        Random random = new Random();
        BytesParser bytesParser =new  BytesParser.Builder()
                .charsetName("GB2312")
                .addJavaType(String.class, new StringJavaDataType("GB2312"))
                .build();
        byte[] temp = new byte[1];
        byte[] info = new byte[4 + 4  + 1 + 10 + 20 + 8 + 8];
        int[] sign = {-1, 1};
        for (int i = 0; i < 100; i++) {
            Student student = new Student();
            student.id = i;
            student.grade = random.nextFloat();
            random.nextBytes(temp);
            student.grade = (byte)(2 * i);
            student.name = getRandomString(3);
            student.accout = getRandomString(4);
            student.number = Long.valueOf(random.nextInt());
            student.lesson =(char) (i * sign[random.nextInt(2)]);
            bytesParser.toBytes(student, info, 0, info.length);
            Student student1 = bytesParser.toBean(Student.class, info);

            Assert.assertEquals(student, student1);
            LoggerUtil.i("\n" + student.toString() + "\n" + student1.toString());


        }
    }





    public static class Person{
        @BytesInfo(order = 1, lenFlag = true)
        char nameLen;
        @BytesInfo(order = 2)
        String name;

        @BytesInfo(order = 3, lenFlag = true)
        char headerUrlLen;
        @BytesInfo(order = 4)
        String headerUrl;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Person person = (Person) o;

            if (nameLen != person.nameLen) return false;
            if (headerUrlLen != person.headerUrlLen) return false;
            if (!name.equals(person.name)) return false;
            return headerUrl.equals(person.headerUrl);
        }

        @Override
        public int hashCode() {
            int result = (int) nameLen;
            result = 31 * result + name.hashCode();
            result = 31 * result + (int) headerUrlLen;
            result = 31 * result + headerUrl.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "nameLen=" + (int)nameLen +
                    ", name='" + name + '\'' +
                    ", headerUrlLen=" + (int)headerUrlLen +
                    ", headerUrl='" + headerUrl + '\'' +
                    '}';
        }
    }

    @Test
    public void nonFixedLength(){

        String charset = "GB2312";

        BytesParser bytesParser =new  BytesParser.Builder()
                .charsetName(charset)
                .addJavaType(String.class, new StringJavaDataType(charset))
                .build();

        Person person =new Person();
        person.name = "一般情况下我们会使用数据的基本数据类型：";
        person.headerUrl = "http://editerupload.eepw.com.cn/201809/61001537857032.jpg";

        byte[] bytes = bytesParser.toBytes(person);
        LoggerUtil.d(person.toString());
        Person person1 = bytesParser.toBean(Person.class, bytes);
        Assert.assertEquals(person, person1);


    }

    public static String getRandomString(int length) {

        String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            //产生0-61的数字
            int number = random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }


    public static class Book{

        @BytesInfo(order = 1)
        int anInt;

        @Override
        public String toString() {
            return "Book{" +
                    "anInt=" + anInt +
                    '}';
        }
    }

    @Test
    public void testInt(){
        BytesParser bytesParser =new  BytesParser.Builder()
                .order(ByteOrder.BIG_ENDIAN)
                .build();
        byte data[] = {0x01, 0x02, 0x03, 0x04};
        Book book = bytesParser.toBean(Book.class, data);
        System.out.println(book.toString());

    }
}
