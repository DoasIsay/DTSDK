package serialize.impl;

import config.FieldConfig;
import config.SerializerConfig;
import serialize.Event;
import serialize.AbstractDeserializer;

import java.util.List;

public class LineDeserializer extends AbstractDeserializer<String> {
    private String split;
    private String eventTime;
    private String format;

    @Override
    public void open(SerializerConfig config) {
        super.open(config);
        split = config.get("split");
        eventTime = config.get("event-time");
        format = config.get("format");
    }

    @Override
    public Event deserialize(String record) {
        String[] fields = record.split(split);
        Event event = new Event();

        for (int i = 0; i < FieldConfigs.size(); ++i)
            event.setField(FieldConfigs.get(i).name, fields[i]);

        event.setEventTime(event.getField(eventTime), format);
        return event;
    }
}
