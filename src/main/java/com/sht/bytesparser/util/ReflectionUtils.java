package com.sht.bytesparser.util;

import com.sht.bytesparser.bean.PrimitiveType;
import com.sht.bytesparser.bean.TypeConst;
import com.sht.bytesparser.exception.InternalException;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils {

    public static boolean isNonParamConstructorExists(Class clazz) {
        try {
            Constructor[] constructors = clazz.getDeclaredConstructors();
            for (Constructor constructor : constructors) {
                if (constructor.getParameterTypes().length == 0) {
                    return true;
                }
            }
        } catch (SecurityException e) {
            throw new InternalException(e);
        }
        return false;
    }
    public static <T extends Annotation> T getAnnotation(Field field, Class<T> annotationClazz) {
        if (field == null || annotationClazz == null) {
            throw new InternalException(
                    "Field [" + field + "] or Annotation Class [" + annotationClazz + "] is NULL");
        }
        try {
            return field.getAnnotation(annotationClazz);
        } catch (ClassCastException e) {
            throw new InternalException(e);
        }
    }
    public static List<Field> extractAnnotatedFields(Class<?> clazz,
                                              Class<? extends Annotation> annotationClazz) {
        return extractAnnotatedFields(clazz, annotationClazz, true);
    }
    private static List<Field> extractAnnotatedFields(Class<?> clazz,
                                              Class<? extends Annotation> annotationClazz,
                                              boolean hierarchically) {
        List<Field> fields = new ArrayList<>();
        do {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(annotationClazz)) {
                    fields.add(field);
                }
            }
            if (!hierarchically) {
                break;
            }
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        return fields;
    }

    /**
     * check class inheritance
     *
     * @return true if child inherited from parent
     */
    public static boolean isParentClass(Class parent, Class child) {
        return parent != null && child != null && parent.isAssignableFrom(child);
    }

    public static Class<?> getCollectionFirstMemberType(Field collectionField) {
        Class<?> collectionType = collectionField.getType();
        if (collectionType.isArray()) {
            return collectionType.getComponentType();
        } else if (TypeConst.isSupportedCollection(collectionType)) {
            return (Class<?>) ((ParameterizedType) collectionField.getGenericType()).getActualTypeArguments()[0];
        }
        return null;
    }

    public static boolean isInterfaceOrAbstract(Class clazz) {
        return clazz != null && (Modifier.isAbstract(clazz.getModifiers()) || Modifier.isInterface(
                clazz.getModifiers()));
    }

    public static <T> T getFieldValue(Object targetObject, Field field) {
        if (targetObject == null || field == null) {
            return null;
        }
        try {
            field.setAccessible(true);
            return (T) field.get(targetObject);
        } catch (SecurityException | ClassCastException | IllegalArgumentException | IllegalAccessException e) {
            throw new InternalException(e);
        }
    }
    public static void setField(Object targetObject, Field field, Object fieldValue) {
        if (targetObject == null || field == null) {
            throw new InternalException("Field [" + field + "] or targetObject [" + targetObject + "] is NULL");
        }
        try {
            field.setAccessible(true);
            field.set(targetObject, fieldValue);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException e) {
            throw new InternalException(e);
        }
    }

    public static <T> T createParamInstance(Class<?> clazz) {
        T object;
        try {
            PrimitiveType processor = TypeConst.findPrimitive(clazz);
            if (processor != null) {
                object = (T) processor.defaultValue();
            } else {
                object = (T) clazz.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InternalException(e);
        }
        if (object == null) {
            object = (T) Array.get(Array.newInstance(clazz, 1), 0);
        }
        return object;
    }
}
