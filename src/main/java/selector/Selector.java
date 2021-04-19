/*
 * Copyright (c) 2021, wenwu xie <870585356@qq.com>
 * All rights reserved.
 */

package selector;

import config.SelectorConfig;
import serialize.Event;

import java.util.Set;

public interface Selector {
    void open(SelectorConfig config);

    Set<String> select(Event event);

    void close();
}
