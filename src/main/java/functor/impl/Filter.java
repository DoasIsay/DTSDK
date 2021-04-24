/*
 * Copyright (c) 2021, wenwu xie <870585356@qq.com>
 * All rights reserved.
 */

package functor.impl;

import annotation.Functor;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import config.FunctorConfig;
import functor.AbstractFunctor;
import functor.Action;
import util.Dictionary;

import java.util.HashMap;
import java.util.Map;

@Functor
public class Filter extends AbstractFunctor {
    private Expression expression;
    private Map<String, Boolean> dictionary;

    @Override
    public void open(FunctorConfig config) {
        super.open(config);
        String express = config.get("express");
        if (express != null) {
            expression = AviatorEvaluator.compile(express, true);
        }

        if (config.get("dictionary") != null && config.get("ns") != null) {
            dictionary = new HashMap<>();
            Dictionary.load(config.get("ns"), config.get("dictionary")).forEach(((k, v) -> dictionary.put(k, (Boolean) v)));
        }
    }

    @Override
    public Action invoke() {
        if (expression != null) {
            return (boolean) expression.execute(event.getBody()) ? Action.BREAK : Action.CONTINUE;
        }
        if (dictionary == null)
            return Action.FAIL;

        Boolean ret = dictionary.get(InFieldValue);
        if (ret == null)
            return Action.CONTINUE;

        return ret ? Action.CONTINUE : Action.BREAK;
    }
}
