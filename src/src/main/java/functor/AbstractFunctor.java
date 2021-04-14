package functor;

import config.FunctorConfig;

import java.util.List;

public abstract class AbstractFunctor implements Functor {
    public FunctorConfig Config;

    public String InFieldName;
    public String OutFieldName;
    public List<String> InFieldNames;
    public List<String> OutFieldNames;
    public String InFieldValue;
    public String[] InFieldValues;

    public Functor open(FunctorConfig config) {
        Config = config;
        InFieldName = Config.getInField();
        InFieldNames = Config.getInFields();
        OutFieldName = Config.getOutField();
        OutFieldNames  = Config.getOutFields();

        return this;
    }

    private Event event;

    private void prepare() {
        InFieldValue = event.getField(InFieldName);
        InFieldValues = new String[InFieldNames.size()];
        for (int i = 0; i < InFieldNames.size(); ++i) {
            InFieldValues[i] = event.getField(InFieldNames.get(i));
        }
    }

    public void setField(String key, Object value) {
        event.setField(key, value);
    }

    public boolean doInvoke(Event event) {
        this.event = event;
        prepare();
        return invoke();
    }
}
