package com.sht.bytesparser.util;

import com.sht.bytesparser.annotation.BytesInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by navy on 2018/11/14.
 * 兼容Android 24以下的版本
 */

public class CompatUtils {

    public  static <T extends Annotation> T getDeclaredAnnotation(Field field, Class<T> annotationClass){
        return field.getAnnotation(annotationClass);
    }

    public static   Object getOrDefault(Map map, Object key, Object defaultValue) {
        Object v;
        return (((v = map.get(key)) != null) || map.containsKey(key))
                ? v
                : defaultValue;
    }

}
