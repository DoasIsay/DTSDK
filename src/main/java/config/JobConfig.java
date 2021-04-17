package config;

import util.Checkable;

import java.util.Map;

public class JobConfig implements Checkable {
    public SourceConfig source;
    public Map<String, SinkConfig> sink;
    public Map<String, ProcessConfig> process;

    public void check() {
        if (source == null)
            throw new RuntimeException("JobConfig source must not null");
        else
            source.check();

        if (sink == null)
            throw new RuntimeException("JobConfig sink must not null");
        else
            sink.forEach((name, config) -> config.check());

        if (process == null)
            throw new RuntimeException("JobConfig process must not null");
        else
            process.forEach((name, config) -> config.check());
    }
}
