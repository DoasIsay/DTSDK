/*
 * Copyright (c) 2021, wenwu xie <870585356@qq.com>
 * All rights reserved.
 */

package selector.impl;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import config.SelectorConfig;
import selector.Selector;
import serialize.Event;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@annotation.Selector
public class RuleEngine implements Selector {
    public static Map<String, Expression> expressionMap = new ConcurrentHashMap<>();
    private Expression expression;

    @Override
    public void open(SelectorConfig config) {
        expression = AviatorEvaluator.compile((String)config.config.get("express"));
        expressionMap.put(config.name, expression);
    }

    @Override
    public Set<String> select(Event event) {
        Set<String> out = new HashSet<>();
        String sinkName = (String) expression.execute(event.getBody());
        System.out.println(sinkName);
        out.add(sinkName);
        return out;
    }

    @Override
    public void close() {

    }
}
