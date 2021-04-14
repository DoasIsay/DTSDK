package serialize;

import config.FieldConfig;
import config.SerializeConfig;

import java.util.List;

public abstract class AbstractDeserializer<T> implements Deserializer<T> {
    public SerializeConfig Config;
    public List<FieldConfig> InFields, OutFields;
    @Override
    public void open(SerializeConfig config) {
        this.Config = config;
        InFields = config.get("inFields");
        OutFields = config.get("outFields");
    }

    @Override
    public void close() {}
}
