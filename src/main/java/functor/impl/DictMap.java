/*
 * Copyright (c) 2021, wenwu xie <870585356@qq.com>
 * All rights reserved.
 */

package functor.impl;

import annotation.Functor;
import functor.Action;
import util.Dictionary;
import config.FunctorConfig;
import functor.AbstractFunctor;

import java.util.List;
import java.util.Map;

@Functor(name="DictMap")
public class DictMap extends AbstractFunctor {
    private Map<String, Object> dictionary;

    @Override
    public void open(FunctorConfig config) {
        super.open(config);
        dictionary = Dictionary.getInstance().load(config.get("namespace"), config.get("dictionary"));
    }

    @Override
    public Action invoke() {
        if (InFieldValue.isEmpty())
            return Action.FAIL;

        List<String> outFieldValues = (List<String>) dictionary.get(InFieldValue);
        if (outFieldValues == null)
            return Action.FAIL;

        assert(OutFieldNames.size() == outFieldValues.size());

        for (int i = 0; i < OutFieldNames.size(); ++i)
            setField(OutFieldNames.get(i), outFieldValues.get(i));

        return Action.SUCCESS;
    }

    @Override
    public void close() {
        dictionary.clear();
    }
}
