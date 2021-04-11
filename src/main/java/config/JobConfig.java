package config;

import java.util.HashMap;
import java.util.List;

public class JobConfig {
    public HashMap<String,Object> source;
    public HashMap<String,Object> sink;
    public List<FieldConfig> inFields;
    public List<FieldConfig> outFields;
    public List<FunctorConfig> functors;
}
