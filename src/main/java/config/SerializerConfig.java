/*
 * Copyright (c) 2021, wenwu xie <870585356@qq.com>
 * All rights reserved.
 */

package config;

import annotation.AnnotationHelper;
import util.Checkable;

import java.util.List;
import java.util.Map;

public class SerializerConfig implements Checkable {
    public String name;
    public List<FieldConfig> fieldConfigs;
    public Map<String, Object> config;
    public <T> T get(String key) { return (T) config.get(key); }

    public void check() {
        if (name == null)
            throw new RuntimeException("SerializerConfig name must not null");
        if (!AnnotationHelper.annotationClass.contains(name))
            throw new RuntimeException("not support serializer: " + name);
    }
}
