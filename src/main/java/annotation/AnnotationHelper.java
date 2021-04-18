/*
 * Copyright (c) 2021, wenwu xie <870585356@qq.com>
 * All rights reserved.
 */

package annotation;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnnotationHelper {
    public static Set<String> annotationClass = new HashSet<>();

    public static Map<String, Class<?>> getAnnotationClass(String path, Class c) {
        Reflections reflections = new Reflections(path);
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(c);

        Map<String, Class<?>> classMap = new HashMap<>();
        classSet.forEach(classType -> {
            try {
                Method method = c.getMethod("name");
                Annotation annotation = classType.getDeclaredAnnotation(c);
                String name = (String) method.invoke(annotation);
                if (name.isEmpty())
                    name = classType.getSimpleName();
                System.out.println("get annotation: " + name);
                classMap.put(name, classType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        annotationClass.addAll(classMap.keySet());
        return classMap;
    }

    public static <T> T getInstance(String name, Map<String, Class<?>> map) {
        Class<T> c = (Class<T>) map.get(name);
        if (c == null)
            throw new RuntimeException("not support: " + name);
        try {
            return (T) c.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("create instance fail: " + name);
        }
    }
}
