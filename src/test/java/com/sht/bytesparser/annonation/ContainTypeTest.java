package com.sht.bytesparser.annonation;

import com.sht.bytesparser.annotation.BytesInfo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by navy on 2018/11/13.
 */

public class ContainTypeTest {

    public static class Order{
        @BytesInfo(order = 1)
        int id;
        @BytesInfo(order = 2, lenFlag = true)
        short addressLen;
        @BytesInfo(order = 3)
        String address;
        @BytesInfo(order = 4, lenFlag = true)
        int goodsNum;
        @BytesInfo(order = 5)
        List<Goods> goods = new ArrayList<>();
    }


    public static class Goods{
        @BytesInfo(order = 1)
        int id;
        @BytesInfo(order = 2, lenFlag = true)
        short nameLen;
        @BytesInfo(order = 3)
        String name;
        @BytesInfo(order = 4)
        float price;
    }
}
