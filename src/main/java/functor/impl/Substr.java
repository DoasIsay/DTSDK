package functor.impl;

import config.FunctorConfig;
import functor.AbstractFunctor;

public class Substr extends AbstractFunctor {
    private int startIdx;
    private int endIdx;

    @Override
    public void open(FunctorConfig config) {
        super.open(config);
        startIdx = config.getInt("start");
        endIdx   = config.getInt("end");
    }

    @Override
    public boolean invoke() {
        if (InFieldValue.length() < endIdx)
            endIdx = InFieldValue.length();

        setField(OutFieldName, InFieldValue.substring(startIdx, endIdx));

        return true;
    }
}
