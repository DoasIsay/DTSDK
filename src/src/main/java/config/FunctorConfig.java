package config;

import java.util.List;
import java.util.Map;

public class FunctorConfig {
    public String name;
    public Map<String, Object> config;

    public <T> T get(String key) {
        return (T) config.get(key);
    }

    private String getField(String key) {
        List<String> fields =  get(key);
        if (fields.size() > 0)
            return fields.get(0);
        else
            return null;
    }

    public String getInField() {
        return getField("inFields");
    }

    public String getOutField() {
        return getField("outFields");
    }

    public List<String> getInFields() {
        List<String> fields = get("inFields");
        return fields;
    }

    public List<String> getOutFields() {
        List<String> fields = get("outFields");
        return fields;
    }
}
