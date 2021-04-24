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

@annotation.Selector
public class RuleEngine implements Selector {
    private List<Expression> expressions;

    @Override
    public void open(SelectorConfig config) {
        expressions = new ArrayList<>();
        List<String> expresss = config.express;
        expresss.forEach(express -> {
            expressions.add(AviatorEvaluator.compile(express, true));
        });
    }

    @Override
    public Set<String> select(Event event) {
        Set<String> sinkNames = new HashSet<>();
        for (Expression expression: expressions) {
            String sinkName = (String) expression.execute(event.getBody());
            switch (sinkName) {
                case "break":
                    return sinkNames;
                case "continue":
                    break;
                default:
                    sinkNames.add(sinkName);
            }
        }

        return sinkNames;
    }

    @Override
    public void close() {

    }
}
