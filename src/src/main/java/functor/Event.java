package functor;

import java.util.Map;

public class Event {
    public String type;
    public long processTime;
    public long eventTime;
    public long ingestTime;

    public Map<String, Object> head;
    public Map<String, Object> body;
    public <T> T getField(String key) { return  (T) body.get(key); }
    public void setField(String key, Object value) { body.put(key, value); }
}
