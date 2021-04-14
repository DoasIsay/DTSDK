package serialize.impl;

import serialize.AbstractSerializer;
import serialize.Event;

public class LineSerializer extends AbstractSerializer {
    @Override
    public byte[] serialize(Event event) {
        return new byte[0];
    }
}
