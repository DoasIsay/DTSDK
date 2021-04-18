/*
 * Copyright (c) 2021, wenwu xie <870585356@qq.com>
 * All rights reserved.
 */

package serialize;

import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Event {
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private volatile static long lastEventTime = System.currentTimeMillis();

    private String type;
    private long processTime;
    private long eventTime;
    private long ingestTime;

    private Map<String, Object> head = new HashMap<>();
    private Map<String, Object> body = new HashMap<>();

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
