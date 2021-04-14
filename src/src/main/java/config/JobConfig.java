package config;

import java.util.List;
import java.util.Map;

public class JobConfig {
    public SourceConfig source;
    public List<SinkConfig> sink;
    public Map<String, ProcessConfig> process;
}
