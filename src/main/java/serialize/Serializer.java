package serialize;

import config.SerializeConfig;

import java.util.ArrayList;
import java.util.List;

public interface Serializer<T> {
    void open(SerializeConfig config);

    void close();

    T serialize(Event event);

    default List<T> serializes(Event event) { return new ArrayList<T>(){{add(serialize(event));}}; }
}
