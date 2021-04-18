/*
 * Copyright (c) 2021, wenwu xie <870585356@qq.com>
 * All rights reserved.
 */

package config;

import util.Checkable;

import java.util.List;

public class OutConfig implements Checkable {
    public List<FieldConfig> fields;
    public SerializerConfig serializer;

    public void check() {
        if (fields == null)
            throw new RuntimeException("OutConfig fields must not null");
        fields.forEach(config -> config.check());

        if (serializer == null)
            throw new RuntimeException("OutConfig serializer must not null");
        serializer.check();
    }
}
