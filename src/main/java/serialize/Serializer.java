package serialize;

import config.SerializeConfig;

import java.util.ArrayList;
import java.util.List;

public interface Serializer {
    void open(SerializeConfig config);

    void close();

    byte[] serialize(Event event);

    default List<byte[]> serializes(Event event) { return new ArrayList<byte[]>(){{add(serialize(event));}}; }
}
