package config;

import util.Checkable;

import java.util.List;

public class InConfig implements Checkable {
    public List<FieldConfig> fields;
    public SerializerConfig serializer;

    public void check() {
        if (fields == null)
            throw new RuntimeException("InConfig fields must not null");
        else
            fields.forEach(config -> config.check());

        if (serializer == null)
            throw new RuntimeException("InConfig serializer must not null");
        else
            serializer.check();
    }
}
