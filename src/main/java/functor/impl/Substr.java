package functor.impl;

import config.FunctorConfig;
import functor.AbstractFunctor;
import functor.Functor;

public class Substr extends AbstractFunctor {
    private int startIdx;
    private int endIdx;

    @Override
    public Functor open(FunctorConfig config) {
        super.open(config);
        startIdx = Config.getInt("start");
        endIdx   = Config.getInt("end");
        return this;
    }

    @Override
    public boolean invoke() {
        if (InFieldValue.length() < endIdx)
            endIdx = InFieldValue.length();

        setField(OutFieldName, InFieldValue.substring(startIdx, endIdx));

        return true;
    }
}
