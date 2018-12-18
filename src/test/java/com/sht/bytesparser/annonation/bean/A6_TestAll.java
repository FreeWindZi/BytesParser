package com.sht.bytesparser.annonation.bean;

import com.sht.bytesparser.BytesParser;
import com.sht.bytesparser.annotation.BytesInfo;
import com.sht.bytesparser.annotation.BytesSerializable;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class A6_TestAll {

    public static class Books implements BytesSerializable{
        @BytesInfo(order = 0)
        private int auther;
        @BytesInfo(order = 1, len = 8)
        private double price;
        @BytesInfo(order = 2, lenFlagBytesSize = 1)
        private String name;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Books books = (Books) o;
            return auther == books.auther &&
                    Double.compare(books.price, price) == 0 &&
                    Objects.equals(name, books.name);
        }

        @Override
        public String toString() {
            return "Books{" +
                    "auther=" + auther +
                    ", price=" + price +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public static class Writer implements BytesSerializable{
        @BytesInfo(order = 0)
        private int id;
        @BytesInfo(order = 1, len = 20)
        private String name;
        @BytesInfo(order = 2, lenFlagBytesSize = 1)
        private List<Books> books ;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Writer writer = (Writer) o;
            return id == writer.id &&
                    Objects.equals(name, writer.name) &&
                    Objects.equals(books, writer.books);
        }

        @Override
        public String toString() {
            return "Writer{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", books=" + books +
                    '}';
        }
    }

    @Test
    public void testBytesParser(){
        BytesParser bytesParser = new BytesParser.Builder()
                .order(ByteOrder.BIG_ENDIAN)
                .charsetName(Charset.forName("GB2312"))
                .build();
        Writer writer = new Writer();
        writer.name = "孙悟空";
        writer.id = 123456789;
        writer.books = new ArrayList<>();
        String[] names = new String[]{"西游记", "红楼梦", "三国演义", "水浒传"};
        double[] prices = new double[]{12.12, 11.11, 30.58, 98.54};
        for (int i = 0; i < names.length; i++){
            Books book = new Books();
            book.auther = writer.id;
            book.name = names[i];
            book.price = prices[i];
            writer.books.add(book);
        }

        float val = 12.12f;
        long v = Double.doubleToLongBits(val);
        int a = Float.floatToIntBits(val);
        System.out.println(v +" " + a);

        byte[] data = bytesParser.toBytes(writer);
        Writer writer1 = bytesParser.toBean(Writer.class, data);
        Assert.assertEquals(writer, writer1);
        System.out.println(writer1.toString());

    }



}
