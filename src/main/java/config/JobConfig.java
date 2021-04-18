/*
 * Copyright (c) 2021, wenwu xie <870585356@qq.com>
 * All rights reserved.
 */

package config;

import util.Checkable;

import java.util.Map;

public class JobConfig implements Checkable {
    public Map<String,SourceConfig> source;
    public Map<String, SinkConfig> sink;
    public Map<String, ProcessConfig> process;

    public void check() {
        if (source == null)
            throw new RuntimeException("JobConfig source must not null");
        source.forEach((name, config) -> config.check());

        if (sink == null)
            throw new RuntimeException("JobConfig sink must not null");
        sink.forEach((name, config) -> config.check());

        if (process == null)
            throw new RuntimeException("JobConfig process must not null");
        process.forEach((name, config) -> config.check());
    }
}
