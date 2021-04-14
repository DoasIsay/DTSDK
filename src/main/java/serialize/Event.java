package serialize;

import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

@Getter
@Setter
public class Event {
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public String type;
    public long processTime;
    public long eventTime;
    public long ingestTime;
    private static long lastEventTime = System.currentTimeMillis();

    public Map<String, Object> head;
    public Map<String, Object> body;
    public <T> T getField(String key) { return  (T) body.get(key); }
    public void setField(String key, Object value) { body.put(key, value); }

    public void setEventTime(String time, String format) {
        if (format.equals("timestamp")) {
            setEventTime(Long.parseLong(time));
            return;
        }

        long timestamp = lastEventTime;
        try {
            lastEventTime = timestamp = df.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setEventTime(timestamp);
    }
}
