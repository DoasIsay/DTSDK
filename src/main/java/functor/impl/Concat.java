package functor.impl;

import functor.AbstractFunctor;

import java.util.List;
import java.util.Map;

public class Concat extends AbstractFunctor {

    //param:   outField,spit,inField0,inField1,inField2....
    //example: lacCi,-,lac,ci
    @Override
    public boolean invoke(Map<String, String> in, Map<String, String> out) {
        String outFieldName = param.get(0);
        String split = param.get(1);

        String outFieldValue = "";
        List<String> inFields = param.subList(2, param.size());
        for (String field: inFields) {
            if (!outFieldValue.isEmpty())
                outFieldValue += split + in.get(field);
            else
                outFieldValue += in.get(field);
        }

        out.put(outFieldName, outFieldValue);
        return true;
    }
}
