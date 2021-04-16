package serialize.impl;

import com.google.gson.Gson;
import serialize.AbstractSerializer;
import serialize.Event;

public class LineSerializer extends AbstractSerializer {
    @Override
    public String serialize(Event event) {
        return new Gson().toJson(event);
    }
}
