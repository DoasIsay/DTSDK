package serialize.impl;

import config.JobConfig;
import config.SerializeConfig;
import functor.Event;
import serialize.AbstractDeserializer;

public class LineDeserializer extends AbstractDeserializer<String> {
    private String split;

    @Override
    public void open(SerializeConfig config) {
        super.open(config);
        split = config.get("split");
    }

    @Override
    public Event deserialize(String record) {
        return null;
    }

    @Override
    public Event deserialize(String type, String record) {
        String[] fields = record.split(split);
        String[] fieldNames = new String[1];
        Event event = new Event();
        event.type = type;

        for (int i = 0; i < fieldNames.length; ++i)
            event.setField(fieldNames[i], fields[i]);
        return event;
    }
}
