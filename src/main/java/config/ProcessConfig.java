package config;

import java.util.List;
import java.util.Map;

public class ProcessConfig {
    public InConfig in;
    public Map<String,OutConfig> out;
    public List<FunctorConfig> functors;
}
