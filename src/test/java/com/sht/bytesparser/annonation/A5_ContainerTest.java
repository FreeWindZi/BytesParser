package com.sht.bytesparser.annonation;

import com.sht.bytesparser.annonation.bean.ArrayBean;
import com.sht.bytesparser.annonation.bean.ContainerBean;
import com.sht.bytesparser.log.LoggerUtil;
import org.junit.Assert;
import org.junit.Test;

public class A5_ContainerTest  extends AbstractTest{

    @Test
    public void containerTest(){
        ContainerBean.Son son = new ContainerBean.Son();
        son.fInt = 1234567;
        son.sonInt = -90;
        son.sonf = 0.123344f;
        son.sonChar = 'a';
        byte data[] = bytesParser.toBytes(son);
        ContainerBean.Son tempSon= bytesParser.toBean(ContainerBean.Son.class, data);
        Assert.assertEquals(tempSon, son);
        LoggerUtil.d(tempSon.toString());
    }
}
