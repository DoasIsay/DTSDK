package config;

import java.util.List;
import java.util.Map;

public class JobConfig {
    public SourceConfig source;
    public Map<String,SinkConfig> sink;
    public Map<String, ProcessConfig> process;
}
