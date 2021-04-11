package functor;

import config.FunctorConfig;

import java.util.List;
import java.util.Map;

public abstract class AbstractFunctor implements Functor {
    private FunctorConfig config;
    public List<String> param;
    public boolean fromOut = false;
    public Functor init(FunctorConfig config) {
        this.config = config;
        param = config.param;
        fromOut = config.from.equals("out");
        return this;
    }

    public boolean invokeCheck(Map<String, String> in, Map<String, String> out) {
        if (!fromOut)
            return invoke(in,out);
        else
            return invoke(out,out);
    }
}
