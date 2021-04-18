/*
 * Copyright (c) 2021, wenwu xie <870585356@qq.com>
 * All rights reserved.
 */

package config;

import util.Checkable;

import java.util.Map;

public class SinkConfig implements Checkable {
    public String name;
    public Map<String, Object> config;

    public void check() {
        if (name == null)
            throw new RuntimeException("SinkConfig name must not null");
        if (config == null)
            throw new RuntimeException("SinkConfig config must not null");
    }
}
