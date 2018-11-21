package com.sht.bytesparser.annonation;

import com.sht.bytesparser.annotation.BytesInfo;
import com.sht.bytesparser.log.LoggerUtil;
import com.sht.bytesparser.parser.bean.Reserved;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by navy on 2018/11/15.
 */

public class ReservedTest  extends AbstractTest{
    public static class Send{
        @BytesInfo(order = 1, len = 3)
        private Reserved reserved;
        @BytesInfo(order = 2)
        private int time;

        public Send() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Send send = (Send) o;

            if (time != send.time) return false;
            return reserved != null ? reserved.equals(send.reserved) : send.reserved == null;
        }

        @Override
        public String toString() {
            return "Send{" +
                    "reserved=" + reserved +
                    ", time=" + time +
                    '}';
        }
    }

    public static class Response{
        @BytesInfo(order = 1)
        private int time;
        @BytesInfo(order = 2, len = 3)
        private Reserved reserved;
        @BytesInfo(order = 3)
        private byte order;


        public Response() {
        }
    }

    @Test
    public void testReserved(){
        Random random = new Random();
        Send send = new Send();
        send.time = random.nextInt();
        byte data[] = bytesParser.toBytes(send);
        LoggerUtil.i(Arrays.toString(data));

        Send send1 = bytesParser.toBean(Send.class, data);
        Assert.assertEquals(7, data.length);
        Assert.assertEquals(send.time, send1.time);
        Assert.assertEquals(send, send1);
        LoggerUtil.i(send1.toString());





        Response response = new Response();
        response.time = 1230272577;
        response.order = (byte) 0xf4;



        byte data1[] = bytesParser.toBytes(response);
        LoggerUtil.i(Arrays.toString(data1));

        Response response1 = bytesParser.toBean(Response.class, data1);
        Assert.assertEquals(8, data1.length);
        Assert.assertEquals(response1.time, 1230272577);
        Assert.assertEquals(response1.order, (byte) 0xf4);
        LoggerUtil.i(response1.toString());
    }

}
