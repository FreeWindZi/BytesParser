package com.sht.bytesparser.annonation;

import com.sht.bytesparser.annonation.bean.ReservedBean;
import com.sht.bytesparser.log.LoggerUtil;
import org.junit.Assert;
import org.junit.Test;

public class A2_ReservedTest extends AbstractTest{

    ReservedBean reservedBean;
    //int temperature = -23;
    int temperature = 2323;
    long time = System.currentTimeMillis();
    int leng = 20;

    @Test
    public void reservedTest() {
        reservedBean = new ReservedBean(temperature, null, time);
        byte bytes[] = bytesParser.toBytes(reservedBean);
        Assert.assertEquals(leng, bytes.length);
        ReservedBean tempReservedBean = bytesParser.toBean(ReservedBean.class, bytes);
        Assert.assertEquals(reservedBean, tempReservedBean);
        Assert.assertEquals(null, tempReservedBean.getReserved());
        LoggerUtil.d(tempReservedBean.toString());

    }
}
