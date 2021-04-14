package functor.impl;

import functor.AbstractFunctor;

public class Substr extends AbstractFunctor {
    @Override
    public boolean invoke() {
        int startIdx = Config.getInt("start");
        int endIdx   = Config.getInt("end");

        if (InFieldValue.length() < endIdx)
            endIdx = InFieldValue.length();

        setField(OutFieldName, InFieldValue.substring(startIdx, endIdx));

        return true;
    }
}
