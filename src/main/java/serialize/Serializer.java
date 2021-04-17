package serialize;

import config.FieldConfig;
import config.SerializerConfig;

import java.util.ArrayList;
import java.util.List;

public interface Serializer<T> {
    void open(SerializerConfig config);

    void close();

    T serialize(Event event);

    default List<T> serializes(List<FieldConfig> fieldConfigs, Event event) { return new ArrayList<T>(){{add(serialize(event));}}; }
}
