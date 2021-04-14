package serialize;

import config.SerializeConfig;

import java.util.ArrayList;
import java.util.List;

public interface Deserializer<T> {
    void open(SerializeConfig config);

    void close();

    Event deserialize(String type, T record);
    default List<Event> deserializes(String type, T record) {return new ArrayList<Event>(){{add(deserialize(type, record));}};}
}
