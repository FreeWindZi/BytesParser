package com.sht.bytesparser.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by navy on 2018/11/9.
 */

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BytesInfo  {

    public static final int INVALID_LEN = 0;

    /**
     * 确定顺序
     */
    int order();
    int len() default INVALID_LEN;

    /**
     * 只支持len == 2时
     * @return
     */
    boolean sign() default true;

    //用于字符串转化
     String charsetName() default "";

     int lenFlagBytesSize() default 0;


}
