package functor.impl;

import functor.AbstractFunctor;
import java.util.Map;

public class Substr extends AbstractFunctor {
    @Override
    public boolean invoke(Map<String, String> in, Map<String, String> out) {
        String outFieldName = param.get(0);
        String inFieldName  = param.get(3);
        int startIdx = Integer.parseInt(param.get(1));
        int endIdx   = Integer.parseInt(param.get(2));

        String inFieldValue = in.get(inFieldName);
        if (inFieldValue.length() < endIdx)
            endIdx = inFieldValue.length();

        out.put(outFieldName, inFieldValue.substring(startIdx, endIdx));

        return true;
    }
}
