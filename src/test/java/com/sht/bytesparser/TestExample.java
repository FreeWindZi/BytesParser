package com.sht.bytesparser;

import com.sht.bytesparser.log.LoggerUtil;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by navy on 2018/11/9.
 */

public class TestExample {


    @Test
    public void testBuffer(){
        char a = (char) -2;
        char b = 22;

        int aI = a;
        int bI = b ;

        char aS = (char) aI;
        char bS = (char) bI;



        LoggerUtil.d(a +"  " + b );
        LoggerUtil.d(aI +"  " + bI );
        LoggerUtil.d(aS +"  " + bS );
    }
}
