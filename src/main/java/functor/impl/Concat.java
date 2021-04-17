package functor.impl;

import config.FunctorConfig;
import functor.AbstractFunctor;

public class Concat extends AbstractFunctor {
    private String split;

    @Override
    public void open(FunctorConfig config) {
        super.open(config);
        split = config.get("split");
    }

    @Override
    public boolean invoke() {
        String outFieldValue = "";

        for (String field: InFieldValues) {
            if (!outFieldValue.isEmpty())
                outFieldValue += split + field;
            else
                outFieldValue += field;
        }

        setField(OutFieldName, outFieldValue);
        return true;
    }
}
