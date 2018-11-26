package com.sht.bytesparser.annonation;

import com.sht.bytesparser.annonation.bean.BaseBean;
import com.sht.bytesparser.annotation.BytesInfo;
import com.sht.bytesparser.log.LoggerUtil;
import org.junit.Assert;
import org.junit.Test;

public class A1_BaseTest extends AbstractTest{

    BaseBean.Template template;
    BaseBean.Template2 template2;

    byte byteValue = 91;
    short shortValue = -23;
    char charValue = 2528;
    int intValue = 25252522;
    long longValue = 58585858585L;
    float floatValue = 0.3698f;
    double doubleValue = 4664.112554454;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        template = new BaseBean.Template(byteValue, shortValue, charValue, intValue, longValue,
                floatValue, doubleValue);
        template2 = new BaseBean.Template2(byteValue, shortValue, charValue, intValue, longValue,
                floatValue, doubleValue);
    }

    @Test
    public void baseTest(){
        byte bytes[] = bytesParser.toBytes(template);
        BaseBean.Template tempTemplate = bytesParser.toBean(BaseBean.Template.class, bytes);
        Assert.assertEquals(template, tempTemplate);

        LoggerUtil.d(tempTemplate.toString());

        byte bytes2[] = bytesParser.toBytes(template);
        BaseBean.Template2 tempTemplate2 = bytesParser.toBean(BaseBean.Template2.class, bytes2);
        Assert.assertEquals(template2, tempTemplate2);
        LoggerUtil.d(tempTemplate2.toString());
    }
}
