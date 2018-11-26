package com.sht.bytesparser.annonation;

import com.sht.bytesparser.annonation.bean.ArrayBean;
import com.sht.bytesparser.annotation.BytesInfo;

import com.sht.bytesparser.log.LoggerUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by navy on 2018/11/15.
 */

public class A4_ArrayTest extends AbstractTest {

    ArrayBean.Template template;
    byte result = 10;
    int tokenId[] = new int[10];
    int res = 500;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        for (int i = 0; i < tokenId.length; i++){
            tokenId[i] = i;
        }
        template = new ArrayBean.Template(result, tokenId, res);
    }
    @Test
    public void arrayTest(){
        byte data[] = bytesParser.toBytes(template);
        ArrayBean.Template tenpTemplate = bytesParser.toBean(ArrayBean.Template.class, data);
        Assert.assertEquals(tenpTemplate, template);
        LoggerUtil.d(tenpTemplate.toString());
    }

}
