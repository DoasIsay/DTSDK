/*
 * Copyright (c) 2021, wenwu xie <870585356@qq.com>
 * All rights reserved.
 */

package config;

import util.Checkable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProcessConfig implements Checkable {
    public InConfig in;
    public Map<String, OutConfig> out;
    public List<FunctorConfig> functors = new ArrayList<>();

    public void check() {
        if (in == null)
            throw new RuntimeException("ProcessConfig in must not null");
        in.check();

        if (out == null)
            throw new RuntimeException("ProcessConfig out must not null");
        out.forEach((name, config) -> config.check());

        functors.forEach(config -> config.check());
    }
}
