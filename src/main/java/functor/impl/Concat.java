package functor.impl;

import config.FunctorConfig;
import functor.AbstractFunctor;
import functor.Functor;

public class Concat extends AbstractFunctor {
    private String split;

    @Override
    public Functor open(FunctorConfig config) {
        super.open(config);
        split = config.get("split");
        return this;
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
