import com.google.gson.Gson;
import config.*;
import functor.Event;
import functor.Functor;
import functor.impl.Concat;
import functor.impl.DictMap;
import functor.impl.Substr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JobProvider {
    private JobConfig jobConfig;
    private static JobProvider jobProvider = new JobProvider("./job.json");

    private JobProvider(String path) {
        Gson gson = new Gson();
        try {
            jobConfig = gson.fromJson(new FileReader(new File(path)), JobConfig.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public JobProvider getInstance() {
        return jobProvider;
    }

    public SourceConfig getSourceConfig() {
        return jobConfig.source;
    }

    public List<SinkConfig> getSinkConfig() {
        return jobConfig.sink;
    }

    public List<FieldConfig> getInFieldConfig(String resId) {
        return jobConfig.process.get(resId).inFields;
    }

    public List<FieldConfig> getOutFieldConfig(String resId) {
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
        if (resId != null)
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
        jobConfig.process
                .keySet()
                .forEach(resId -> {
                    getFunctors(resId).forEach(functor -> {
                        functor.doInvoke(event);
                    });
                });
    }

    public static void main(String[] args) {
        JobProvider jobProvider = new JobProvider("src/job.json");
        List<Functor> functors = jobProvider.getFunctors("resid");
        HashMap<String, String> in = new HashMap<String, String>() {{
            put("field0", "123");
            put("field1", "456");
        }};

        HashMap<String, String> out = new HashMap<String, String>();
        functors.get(0).invoke();
        System.out.println(out);

        functors.get(1).invoke();
        System.out.println(out);


        functors.get(2).invoke();
        System.out.println(out);
    }
}

