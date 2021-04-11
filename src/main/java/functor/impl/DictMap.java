package functor.impl;

import functor.AbstractFunctor;

import java.util.HashMap;
import java.util.Map;

public class DictMap extends AbstractFunctor {
    private HashMap<String, String> dictionary;

    @Override
    public void open(Map<String, String> config) {
        dictionary = new HashMap<>();
        //load dictionary
        //loadDictionary(config.get("path"));
    }

    @Override
    public boolean invoke(Map<String, String> in, Map<String, String> out) {
        String outFieldName = param.get(0);
        String inFieldName  = param.get(1);
        String inFieldValue = in.get(inFieldName);

        if (inFieldValue.isEmpty())
            return false;

        out.put(outFieldName, dictionary.get(inFieldValue));
        return true;
    }

    @Override
    public void close() {
        dictionary.clear();
    }
}
