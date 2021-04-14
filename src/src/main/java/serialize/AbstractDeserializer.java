package serialize;

import config.SerializeConfig;

public abstract class AbstractDeserializer<T> implements Deserializer<T> {
    public SerializeConfig Config;

    @Override
    public void open(SerializeConfig config) {
        this.Config = config;
    }

    @Override
    public void close() {}
}
