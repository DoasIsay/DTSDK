package config;

import util.Checkable;

import java.util.List;

public class OutConfig implements Checkable {
    public List<FieldConfig> fields;
    public SerializerConfig serializer;

    public void check() {
        if (fields == null)
            throw new RuntimeException("OutConfig fields must not null");
        else
            fields.forEach(config -> config.check());

        if (serializer == null)
            throw new RuntimeException("OutConfig serializer must not null");
        else
            serializer.check();
    }
}
