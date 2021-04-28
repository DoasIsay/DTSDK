/*
 * Copyright (c) 2021, wenwu xie <870585356@qq.com>
 * All rights reserved.
 */

package functor;

import config.FunctorConfig;
import serialize.Event;

public interface Functor {
    void open(FunctorConfig config);

    Action invoke(Event event);

    Action invoke();

    void close();

    String getName();

    void setName(String name);
}
