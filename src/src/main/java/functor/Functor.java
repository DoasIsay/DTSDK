package functor;

import config.FunctorConfig;

public interface Functor {
    Functor open(FunctorConfig config);

    boolean doInvoke(Event event);
    boolean invoke();

    default void close() {}
}
