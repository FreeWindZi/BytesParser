package com.sht.bytesparser.annonation;

import com.sht.bytesparser.BytesParser;
import com.sht.bytesparser.parser.StringJavaDataType;

import org.junit.After;
import org.junit.Before;

import java.nio.ByteOrder;

/**
 * Created by navy on 2018/11/14.
 */

public class AbstractTest {

    protected BytesParser bytesParser;
    protected String charsetName = "GB2312";

    @Before
    public void setUp() throws Exception {
        
        bytesParser = new BytesParser.Builder()
                .order(ByteOrder.BIG_ENDIAN)
                .addJavaType(String.class, new StringJavaDataType(charsetName))
                .build();
    }

    @After
    public void tearDown() throws Exception {


    }

}
