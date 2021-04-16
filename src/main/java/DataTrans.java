import config.*;
import functor.Functor;
import functor.impl.Concat;
import functor.impl.DictMap;
import functor.impl.Substr;
import serialize.Deserializer;
import serialize.Event;
import serialize.Serializer;
import serialize.impl.LineDeserializer;
import serialize.impl.LineSerializer;
import util.GsonHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DataTrans {
    private JobConfig jobConfig;
    private Deserializer deserializer = null;
    private Serializer serializer = null;

    public DataTrans(String path) {
        jobConfig = GsonHelper.get(path, JobConfig.class);
    }

    private HashMap<String, Deserializer> deserializerMap = new HashMap<>();
    public Deserializer getDeserializer(String resId) {
        Deserializer deserializer = deserializerMap.get(resId);
        if (deserializer != null)
            return deserializer;

        System.out.println(resId);
        switch ((String)jobConfig.process.get(resId).in.config.get("name")) {
            case "LineDeserializer":
               deserializer = new LineDeserializer();
               break;
        }
        deserializerMap.put(resId, deserializer);
        return deserializer;
    }

    private HashMap<String, Serializer> serializerMap = new HashMap<>();
    public Serializer getSerializer(String resId, String outType) {
        Serializer serializer = serializerMap.get(resId);
        if (serializer != null)
            return serializer;

        switch ((String)jobConfig.process.get(resId).out.get(outType).config.get("name")) {
            case "LineSerializer":
                serializer = new LineSerializer();
                break;
        }
        serializerMap.put(resId, serializer);
        return serializer;
    }

    public SourceConfig getSourceConfig() {
        return jobConfig.source;
    }

    public Map<String,SinkConfig> getSinksConfig() {
        return jobConfig.sink;
    }

    public List<FieldConfig> getInFieldsConfig(String resId) {
        return jobConfig.process.get(resId).in.fields;
    }

    public List<FieldConfig> getOutFieldsConfig(String resId, String out) {
        return jobConfig.process.get(resId).out.get(out).fields;
    }

    public Set<String> getResIds() {
        return jobConfig.process.keySet();
    }

    private Functor getFunctor(FunctorConfig config) {
        switch (config.name) {
            case "Concat":
                return new Concat().open(config);
            case "Substr":
                return new Substr().open(config);
            case "DictMap":
                return new DictMap().open(config);
            default:
                return null;
        }
    }

    private HashMap<String, List<Functor>> functorsMap = new HashMap<>();

    public List<Functor> getFunctors(String resId) {
        List<Functor> functors = functorsMap.get(resId);
        if (functors != null)
            return functors;

        functors = jobConfig.process.get(resId).functors
                .stream()
                .map(config -> {
                    return getFunctor(config);
                })
                .collect(Collectors.toList());

        functorsMap.put(resId, functors);
        return functors;
    }

    public <IN,OUT> OUT process(String type, IN record) {
        Deserializer deserializer = getDeserializer(type);
        Event event = deserializer.deserialize(type, record);
        event.setIngestTime(System.currentTimeMillis());
        event.setProcessTime(event.getIngestTime());

        doProcess(event);

        jobConfig.process.get(type).out.forEach((k,v)-> {
            Serializer serializer = getSerializer(type,v.config.name);
            System.out.println(serializer.serialize(event));
        });
        //return (OUT) serializer.serialize(event);
        return null;
    }

    public void doProcess(Event event) {
        getFunctors(event.getType()).forEach(functor -> {
            functor.doInvoke(event);
        });
    }

    public static void main(String[] args) {
        DataTrans dataTrans = new DataTrans("src/job.json");
        List<Functor> functors = dataTrans.getFunctors("resid");
        Map<String, Object> in = new HashMap<String, Object>() {{
            put("field0", "123");
            put("field1", "456");
        }};

        Event event = new Event();
        event.setBody(in);
        HashMap<String, String> out = new HashMap<String, String>();
        functors.get(0).doInvoke(event);
        System.out.println(in);

        functors.get(1).doInvoke(event);
        System.out.println(in);


        functors.get(2).doInvoke(event);
        System.out.println(in);
        dataTrans.process("resid", event);
    }
}
