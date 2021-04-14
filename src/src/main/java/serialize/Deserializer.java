package serialize;

import config.SerializeConfig;
import functor.Event;

import java.util.ArrayList;
import java.util.List;

public interface Deserializer<T> {
    void open(SerializeConfig config);
    void close();
    Event deserialize(T record);
    Event deserialize(String type, T record);

    default List<Event> deserializes(T record) {return new ArrayList<Event>(){{add(deserialize(record));}};}
    default List<Event> deserializes(String type, T record) {return new ArrayList<Event>(){{add(deserialize(type, record));}};}
}
