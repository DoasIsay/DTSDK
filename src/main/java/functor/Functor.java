package functor;

import config.FunctorConfig;
import serialize.Event;

public interface Functor {
    void open(FunctorConfig config);

    boolean doInvoke(Event event);

    boolean invoke();

    void close();
}
