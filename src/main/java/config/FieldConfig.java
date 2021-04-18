/*
 * Copyright (c) 2021, wenwu xie <870585356@qq.com>
 * All rights reserved.
 */

package config;

import util.Checkable;

public class FieldConfig implements Checkable {
    public String name;
    public String type = "String";
    public int length  = 0;

    public void check() {
        if (name == null)
            throw new RuntimeException("FieldConfig name must not null");
    }
}