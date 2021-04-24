/*
 * Copyright (c) 2021, wenwu xie <870585356@qq.com>
 * All rights reserved.
 */

package functor.impl;

import annotation.Functor;
import config.FunctorConfig;
import functor.AbstractFunctor;
import functor.Action;

@Functor(name = "Concat")
public class Concat extends AbstractFunctor {
    private String split;

    @Override
    public void open(FunctorConfig config) {
        super.open(config);
        split = config.get("split");
    }

    @Override
    public Action invoke() {
        String outFieldValue = "";

        for (String field: InFieldValues) {
            if (!outFieldValue.isEmpty())
                outFieldValue += split + field;
            else
                outFieldValue += field;
        }

        setField(OutFieldName, outFieldValue);
        return Action.SUCCESS;
    }
}
