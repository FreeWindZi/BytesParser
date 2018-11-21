package com.sht.bytesparser.reflect;

import com.sht.bytesparser.annotation.BytesInfo;
import com.sht.bytesparser.log.LoggerUtil;

import org.junit.Test;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;



/**
 * Created by navy on 2018/11/15.
 */

public class FieldTest {

    public static class Person{

//        @BytesInfo(order = 1)
//        private int a;
        @BytesInfo(order = 3)
        private int[] aArray;
//        @BytesInfo(order = 4)
//        private List<Integer> list;

    }

    @Test
    public void testField(){

        Class<?> clz = Person.class;
        for (Field field : clz.getDeclaredFields()) {

            System.out.println(field.getName());
            System.out.println(field.getType());
            System.out.println(field.getModifiers());
            System.out.println(field.getType().isArray());
            System.out.println(field.getType().getComponentType());
//            Type type =
//
//            //System.out.println(field.getGenericType());
//            System.out.println();
        }

    }


}
