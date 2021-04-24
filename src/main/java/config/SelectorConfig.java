/*
 * Copyright (c) 2021, wenwu xie <870585356@qq.com>
 * All rights reserved.
 */

package config;

import util.Checkable;

import java.util.List;

public class SelectorConfig implements Checkable {
    public String name;
    public List<String> express;

    @Override
    public void check() {
        if (name == null)
            throw new RuntimeException("SelectorConfig name must not null");
        if (express == null)
            throw new RuntimeException("SelectorConfig express must not null");
    }
}