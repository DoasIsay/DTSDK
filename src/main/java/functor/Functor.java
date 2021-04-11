package functor;

import java.util.Map;

public interface Functor {
    default void open(Map<String,String> config) {}

    boolean invokeCheck(Map<String,String> in, Map<String,String> out);
    boolean invoke(Map<String,String> in, Map<String,String> out);

    default void close() {}
}
