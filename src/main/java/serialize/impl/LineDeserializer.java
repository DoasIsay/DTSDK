package serialize.impl;

import config.SerializeConfig;
import serialize.Event;
import serialize.AbstractDeserializer;

public class LineDeserializer extends AbstractDeserializer<String> {
    private String split;
    private String eventTime;
    private String format;

    @Override
    public void open(SerializeConfig config) {
        super.open(config);
        split = config.get("split");
        eventTime = config.get("event-time");
        format = config.get("format");
    }

    @Override
    public Event deserialize(String type, String record) {
        String[] fields = record.split(split);
        String[] fieldNames = new String[1];
        Event event = new Event();
        event.setType(type);

        for (int i = 0; i < fieldNames.length; ++i)
            event.setField(fieldNames[i], fields[i]);

        event.setEventTime(event.getField(eventTime), format);
        return event;
    }
}
