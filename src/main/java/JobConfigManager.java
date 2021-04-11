import com.google.gson.Gson;
import config.FieldConfig;
import config.FunctorConfig;
import config.JobConfig;
import functor.Functor;
import functor.impl.Concat;
import functor.impl.DictMap;
import functor.impl.Substr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class JobConfigManager {
    private JobConfig jobConfig;

    public JobConfigManager(String path) {
        Gson gson = new Gson();
        try {
            jobConfig = gson.fromJson(new FileReader(new File(path)), JobConfig.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Object> getSourceConfig() {
        return jobConfig.source;
    }

    public HashMap<String, Object> getSinkConfig() {
        return jobConfig.sink;
    }

    public List<FieldConfig> getInFieldsConfig() {
        return jobConfig.inFields;
    }

    public List<FieldConfig> getOutFieldsConfig() {
        return jobConfig.inFields;
    }

    private Functor getFunctor(FunctorConfig config) {
        switch (config.name) {
            case "Concat":
                return new Concat().init(config);
            case "Substr":
                return new Substr().init(config);
            case "DictMap":
                return new DictMap().init(config);
            default:
                return null;
        }
    }

    public List<Functor> getFunctors() {
        return jobConfig.functors.stream().map(config -> {
            return getFunctor(config);
        }).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        JobConfigManager jobConfigManager = new JobConfigManager("src/job.json");
        List<Functor> functors = jobConfigManager.getFunctors();
        HashMap<String, String> in = new HashMap<String, String>(){{
            put("field0","123");
            put("field1","456");
        }};

        HashMap<String, String> out = new HashMap<String, String>();
        functors.get(0).invokeCheck(in, out);
        System.out.println(out);

        functors.get(1).invokeCheck(in, out);
        System.out.println(out);
    }
}

