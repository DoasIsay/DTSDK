/*
 * Copyright (c) 2021, wenwu xie <870585356@qq.com>
 * All rights reserved.
 */

package serialize;

import config.FieldConfig;
import config.SerializerConfig;

import java.util.List;

public abstract class AbstractDeserializer<T> implements Deserializer<T> {
    public SerializerConfig Config;
    public List<FieldConfig> FieldConfigs;
    @Override
    public void open(SerializerConfig config) {
        this.Config = config;
        this.FieldConfigs = config.fieldConfigs;
    }

    @Override
    public void close() {}
}
