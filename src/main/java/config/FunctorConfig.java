/*
 * Copyright (c) 2021, wenwu xie <870585356@qq.com>
 * All rights reserved.
 */

package config;

import annotation.AnnotationHelper;
import util.Checkable;

import java.util.List;
import java.util.Map;

public class FunctorConfig implements Checkable {
    public String name;
    public Map<String, Object> config;

    public void check() {
        if (name == null)
            throw new RuntimeException("FunctorConfig name must not null");
        if (!AnnotationHelper.annotationClass.contains(name))
            throw new RuntimeException("not support functor: " + name);
        if (config == null)
            throw new RuntimeException("FunctorConfig config must not null");

    }

    public <T> T get(String key) {
        return (T) config.get(key);
    }

    public int getInt(String key) {
        return Integer.parseInt((String)config.get(key));
    }
    private String getField(String key) {
        List<String> fields =  get(key);
        if (fields.size() > 0)
            return fields.get(0);
        else
            return null;
    }

    public String getInField() {
        return getField("inFields");
    }

    public String getOutField() {
        return getField("outFields");
    }

    public List<String> getInFields() {
        List<String> fields = get("inFields");
        return fields;
    }

    public List<String> getOutFields() {
        List<String> fields = get("outFields");
        return fields;
    }
}
