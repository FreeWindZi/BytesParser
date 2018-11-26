package com.sht.bytesparser.annonation;

import com.sht.bytesparser.annonation.bean.BaseBean;
import com.sht.bytesparser.annonation.bean.ReservedBean;
import com.sht.bytesparser.annonation.bean.StringBean;
import com.sht.bytesparser.log.LoggerUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class A3_StringTest extends AbstractTest{

    StringBean.Template template;
    String accout = "I am a Java Parse";
    String password = "password";
    int size = 36;

    StringBean.Template2 template2;
    String name = "The temperature drops to -30 and very few people attempt to cross the Pass";
    String headerUrl = "https://projecteuler.net/images/euler_portrait.png";

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        template = new StringBean.Template(accout, password);
        template2 =new StringBean.Template2(name, headerUrl);
    }

    @Test
    public void stringFixLengTest() {
        byte bytes[] = bytesParser.toBytes(template);
        StringBean.Template tempTemplate = bytesParser.toBean(StringBean.Template.class, bytes);
        Assert.assertEquals(36, bytes.length);
        Assert.assertEquals(template, tempTemplate);
        LoggerUtil.d(tempTemplate.toString());
    }


    @Test
    public void stringNotFixLengTest() {
        byte bytes[] = bytesParser.toBytes(template2);
        StringBean.Template2 tempTemplate2 = bytesParser.toBean(StringBean.Template2.class, bytes);
        Assert.assertEquals(template2, tempTemplate2);
        LoggerUtil.d(tempTemplate2.toString());
    }
}
