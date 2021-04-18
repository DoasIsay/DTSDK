/*
 * Copyright (c) 2021, wenwu xie <870585356@qq.com>
 * All rights reserved.
 */

import config.*;
import functor.Functor;
import serialize.Deserializer;
import serialize.Event;
import serialize.Serializer;
import annotation.AnnotationHelper;
import util.Checkable;
import util.GsonHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DataTrans implements Checkable {
    private JobConfig jobConfig;
    private Map<String, Class<?>> functorClassMap = new HashMap<>();
    private Map<String, Class<?>> serializerClassMap = new HashMap<>();
    private Map<String, Class<?>> deserializerClassMap = new HashMap<>();

    public DataTrans(String path) {
        jobConfig = GsonHelper.get(path, JobConfig.class);
    }

    public void start() {
        functorClassMap = AnnotationHelper.getAnnotationClass("functor.impl", annotation.Functor.class);
        serializerClassMap = AnnotationHelper.getAnnotationClass("serialize.impl", annotation.Serializer.class);
        deserializerClassMap = AnnotationHelper.getAnnotationClass("serialize.impl", annotation.Deserializer.class);
        jobConfig.check();
    }

    public void stop() {
        functorMap.forEach((type, functors) -> functors.forEach(functor -> functor.close()));
    }

    public SourceConfig getSourceConfig(String name) {
        return jobConfig.source.get(name);
    }

    public SinkConfig getSinkConfig(String name) {
        return jobConfig.sink.get(name);
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

        Deserializer deserializer = getDeserializer(type, processConfig.in);

        Event event = deserializer.deserialize(inRecord);
        event.setType(type);
        event.setIngestTime(System.currentTimeMillis());
        event.setProcessTime(event.getIngestTime());

        doProcess(event);

        Map<String, Object> outRecord = new HashMap<>();
        processConfig.out.forEach((sinkName, outConfig) -> {
            Serializer serializer = getSerializer(type, sinkName, outConfig);
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

    private Deserializer getDeserializer(String type, InConfig inConfig) {
        Deserializer deserializer = deserializerMap.get(type);
        if (deserializer != null)
            return deserializer;

        SerializerConfig config = inConfig.serializer;
        deserializer = AnnotationHelper.getInstance(config.name, deserializerClassMap);
        //hack config
        config.fieldConfigs = inConfig.fields;
        deserializer.open(config);
        deserializerMap.put(type, deserializer);
        return deserializer;
    }

    private HashMap<String, Serializer> serializerMap = new HashMap<>();

    private SerializerConfig getSerializerConfig(String type, String sinkName) {
        return jobConfig.process.get(type).out.get(sinkName).serializer;
    }

    private Serializer getSerializer(String type, String sinkName, OutConfig outConfig) {
        Serializer serializer = serializerMap.get(type+sinkName);
        if (serializer != null)
            return serializer;

        SerializerConfig config = outConfig.serializer;
        serializer = AnnotationHelper.getInstance(config.name, serializerClassMap);
        //hack config
        config.fieldConfigs = outConfig.fields;
        serializer.open(config);
        serializerMap.put(type+sinkName, serializer);
        return serializer;
    }

    private HashMap<String, List<Functor>> functorMap = new HashMap<>();

    private List<Functor> getFunctors(String type) {
        List<Functor> functors = functorMap.get(type);
        if (functors != null)
            return functors;

        functors = jobConfig.process.get(type).functors
                .stream()
                .map(config -> {
                    Functor functor = AnnotationHelper.getInstance(config.name, functorClassMap);
                    functor.open(config);
                    return functor;
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

        System.out.println(dataTrans.process("type1", "1,2,3,4,5,6,7"));

        List<Functor> functors = dataTrans.getFunctors("type2");
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
