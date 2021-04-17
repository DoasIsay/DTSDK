import config.*;
import functor.Functor;
import functor.impl.Concat;
import functor.impl.DictMap;
import functor.impl.Substr;
import serialize.Deserializer;
import serialize.Event;
import serialize.Serializer;
import serialize.impl.JsonSerializer;
import serialize.impl.LineDeserializer;
import serialize.impl.LineSerializer;
import util.Checkable;
import util.GsonHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DataTrans implements Checkable {
    private JobConfig jobConfig;

    public DataTrans(String path) {
        jobConfig = GsonHelper.get(path, JobConfig.class);
    }

    public void start() {
        jobConfig.check();
    }

    public void stop() {
        functorMap.forEach((type, functors) -> functors.forEach(functor -> functor.close()));
    }

    public SourceConfig getSourceConfig() {
        return jobConfig.source;
    }

    public SinkConfig getSinkConfig(String type) {
        return jobConfig.sink.get(type);
    }

    public List<FieldConfig> getInFieldConfig(String type) {
        return jobConfig.process.get(type).in.fields;
    }

    public List<FieldConfig> getOutFieldConfig(String type, String sinkName) {
        return jobConfig.process.get(type).out.get(sinkName).fields;
    }

    public Set<String> getType() {
        return jobConfig.process.keySet();
    }

    public <IN> Map process(String type, IN inRecord) {
        /*
            only make sure that type is supported here,
            we use Checkable to guarantee no NullPointException,
            cause by config, after DataTrans started
        */
        ProcessConfig processConfig = jobConfig.process.get(type);
        if (processConfig == null)
            throw new RuntimeException("not support type: "+ type);

        Deserializer deserializer = getDeserializer(type);

        Event event = deserializer.deserialize(inRecord);
        event.setType(type);
        event.setIngestTime(System.currentTimeMillis());
        event.setProcessTime(event.getIngestTime());

        doProcess(event);

        Map<String, Object> outRecord = new HashMap<>();
        processConfig.out.forEach((sinkName, outConfig) -> {
            Serializer serializer = getSerializer(type, sinkName);
            outRecord.put(sinkName, serializer.serialize(event));
        });

        return outRecord;
    }

    private void doProcess(Event event) {
        getFunctors(event.getType()).forEach(functor -> { functor.doInvoke(event); });
    }

    private HashMap<String, Deserializer> deserializerMap = new HashMap<>();

    private SerializerConfig getDeserializerConfig(String type) {
        return jobConfig.process.get(type).in.serializer;
    }

    private Deserializer getDeserializer(String type) {
        Deserializer deserializer = deserializerMap.get(type);
        if (deserializer != null)
            return deserializer;

        SerializerConfig config = getDeserializerConfig(type);
        switch (config.name) {
            case "LineDeserializer":
                deserializer = new LineDeserializer();
                break;
            default:
                throw new RuntimeException("not support deserializer: " + config.name);
        }

        //hack config
        config.fieldConfigs = getInFieldConfig(type);
        deserializer.open(config);
        deserializerMap.put(type, deserializer);
        return deserializer;
    }

    private HashMap<String, Serializer> serializerMap = new HashMap<>();

    private SerializerConfig getSerializerConfig(String type, String sinkName) {
        return jobConfig.process.get(type).out.get(sinkName).serializer;
    }

    private Serializer getSerializer(String type, String sinkName) {
        Serializer serializer = serializerMap.get(type+sinkName);
        if (serializer != null)
            return serializer;

        SerializerConfig config = getSerializerConfig(type, sinkName);
        switch (config.name) {
            case "LineSerializer":
                serializer = new LineSerializer();
                break;
            case "JsonSerializer":
                serializer = new JsonSerializer();
                break;
            default:
                throw new RuntimeException("not support serializer: " + config.name);
        }
        //hack config
        config.fieldConfigs = getOutFieldConfig(type, sinkName);
        serializer.open(config);
        serializerMap.put(type+sinkName, serializer);
        return serializer;
    }

    private Functor getFunctor(FunctorConfig config) {
        Functor functor;
        switch (config.name) {
            case "Concat":
                functor = new Concat();
                break;
            case "Substr":
                functor = new Substr();
                break;
            case "DictMap":
                functor = new DictMap();
                break;
            default:
                throw new RuntimeException("not support functor: " + config.name);
        }

        functor.open(config);
        return functor;
    }

    private HashMap<String, List<Functor>> functorMap = new HashMap<>();

    private List<Functor> getFunctors(String type) {
        List<Functor> functors = functorMap.get(type);
        if (functors != null)
            return functors;

        functors = jobConfig.process.get(type).functors
                .stream()
                .map(config -> {
                    return getFunctor(config);
                })
                .collect(Collectors.toList());

        functorMap.put(type, functors);
        return functors;
    }

    @Override
    public void check() {
        jobConfig.check();
    }

    public static void main(String[] args) {
        DataTrans dataTrans = new DataTrans("src/job.json");
        dataTrans.start();

        System.out.println(dataTrans.process("resid", "1,2,3,4,5,6,7"));

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

        dataTrans.stop();
    }
}
