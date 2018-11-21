package com.sht.bytesparser.annonation;

import com.sht.bytesparser.annotation.BitsInfo;
import com.sht.bytesparser.annotation.BytesInfo;
import com.sht.bytesparser.annotation.OrderInfo;
import com.sht.bytesparser.util.BytesParserUtils;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Created by navy on 2018/11/9.
 */

public class OrderInfoAnnotionTest {

    @Test
    public void testOrder(){
        List<Field> fields = BytesParserUtils.getParsableFieldsSortedByOrder(MyName.class);
       int a[] = new int[1];
        fields.stream().forEach( f -> {
            char c = (char) (a[0] + 'a');
            assertEquals(f.getName(), (c) + "");
            a[0]++;
            System.out.println(f.getName());
        });

//        Optional< String > firstName = Optional.ofNullable( null );
//        System.out.println( "First Name is set? " + firstName.isPresent() );
//        System.out.println( "First Name: " + firstName.orElseGet( () -> "[none]" ) );
//        System.out.println( firstName.map( s -> s + "asdasdasd" ).orElse( "Hey Stranger!" ) );
//        System.out.println();
    }

    static String B() {
        System.out.println("B()...");
        return "B";
    }
    @Test
    public void main() {
        System.out.println(Optional.ofNullable("aaa").map(s -> s + "asdasdasd" ).orElse(B()));
        System.out.println(Optional.of("A").orElseGet(() -> B()));
    }

    public static class  MyName{
        @BitsInfo(order = 1, len = 2)
        int a;
        @BitsInfo(order = 2, len = 2)
        int b;
        @BytesInfo(order = 3, len = 4)
        int c;
        @BitsInfo(order = 4, len = 1)
        int d;
        @BytesInfo(order = 5, len = 4)
        int e;


    }

}
