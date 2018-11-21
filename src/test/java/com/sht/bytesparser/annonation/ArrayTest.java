package com.sht.bytesparser.annonation;

import com.sht.bytesparser.annotation.BytesInfo;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by navy on 2018/11/15.
 */

public class ArrayTest extends AbstractTest {

    public static class Response{
        @BytesInfo(order = 1)
        private byte result;
        @BytesInfo(order = 2, len = 3)
        private int[] tokenId;
        @BytesInfo(order = 4)
        private int res;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Response response = (Response) o;

            if (result != response.result) return false;
            if (res != response.res) return false;
            return Arrays.equals(tokenId, response.tokenId);
        }

        @Override
        public int hashCode() {
            int result1 = (int) result;
            result1 = 31 * result1 + Arrays.hashCode(tokenId);
            result1 = 31 * result1 + res;
            return result1;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "result=" + result +
                    ", tokenId=" + Arrays.toString(tokenId) +
                    ", res=" + res +
                    '}';
        }
    }

    @Test
    public void testArray(){
        Response response = new Response();
        response.result = 23;
        response.tokenId = new int[3];
        response.tokenId[0] = 0xffff;
        response.tokenId[1] = 52;
        response.tokenId[2] = 34;
        response.res = 78;

        byte data[] = bytesParser.toBytes(response);
        System.out.println(Arrays.toString(data));

        Response response1 = bytesParser.toBean(Response.class, data);
        Assert.assertEquals(response, response1);
        System.out.println(response);

    }

}
