package config;

import util.Checkable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProcessConfig implements Checkable {
    public InConfig in;
    public Map<String, OutConfig> out;
    public List<FunctorConfig> functors = new ArrayList<>();

    public void check() {
        if (in == null)
            throw new RuntimeException("ProcessConfig in must not null");
        else
            in.check();

        if (out == null)
            throw new RuntimeException("ProcessConfig out must not null");
        else
            out.forEach((name, config) -> config.check());

        functors.forEach(config -> config.check());
    }
}
