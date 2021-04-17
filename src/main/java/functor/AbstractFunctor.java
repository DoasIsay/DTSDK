package functor;

import config.FunctorConfig;
import serialize.Event;

import java.util.List;

public abstract class AbstractFunctor implements Functor {
    public FunctorConfig Config;

    public String InFieldName;
    public String OutFieldName;
    public List<String> InFieldNames;
    public List<String> OutFieldNames;
    public String InFieldValue;
    public String[] InFieldValues;

    @Override
    public void open(FunctorConfig config) {
        Config = config;
        InFieldName = Config.getInField();
        InFieldNames = Config.getInFields();
        OutFieldName = Config.getOutField();
        OutFieldNames  = Config.getOutFields();
    }

    private Event event;

    private void prepare() {
        InFieldValue = getField(InFieldName);

        int size = InFieldNames.size();
        if (size == 1) {
            return;
        }

        InFieldValues = new String[size];
        for (int i = 0; i < size; ++i) {
            InFieldValues[i] = getField(InFieldNames.get(i));
        }
    }

    public <T> T getField(String key) { return event.getField(key); }

    public void setField(String key, Object value) {
        event.setField(key, value);
    }

    public <T> T getInField() { return (T) InFieldValue; }
    public <T> T getInFields() { return (T) InFieldValues; }

    @Override
    public boolean doInvoke(Event event) {
        this.event = event;
        prepare();
        return invoke();
    }

    @Override
    public void close() {}
}
