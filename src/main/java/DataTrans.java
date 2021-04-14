import config.*;
import functor.Functor;
import functor.impl.Concat;
import functor.impl.DictMap;
import functor.impl.Substr;
import serialize.Event;
import util.GsonHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DataTrans {
    private JobConfig jobConfig;

    public DataTrans(String path) {
        jobConfig = GsonHelper.get(path, JobConfig.class);
    }

    public SourceConfig getSourceConfig() {
        return jobConfig.source;
    }

    public List<SinkConfig> getSinksConfig() {
        return jobConfig.sink;
    }

    public List<FieldConfig> getInFieldsConfig(String resId) {
        return jobConfig.process.get(resId).inFields;
    }

    public List<FieldConfig> getOutFieldsConfig(String resId) {
        return jobConfig.process.get(resId).outFields;
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

    public void process(Event event) {
        event.setProcessTime(System.currentTimeMillis());

        jobConfig.process
                .keySet()
                .forEach(resId -> {
                    getFunctors(resId).forEach(functor -> {
                        functor.doInvoke(event);
                    });
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
    }
}
