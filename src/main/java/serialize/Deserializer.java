package serialize;

import config.FieldConfig;
import config.SerializerConfig;

import java.util.ArrayList;
import java.util.List;

public interface Deserializer<T> {
    void open(SerializerConfig config);

    void close();

    Event deserialize(T record);
    default List<Event> deserializes(T record) {return new ArrayList<Event>(){{add(deserialize(record));}};}
}
