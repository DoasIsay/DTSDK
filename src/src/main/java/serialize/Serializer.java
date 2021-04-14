package serialize;

import functor.Event;

public interface Serializer {
    byte[] serialize(Event event);
}
