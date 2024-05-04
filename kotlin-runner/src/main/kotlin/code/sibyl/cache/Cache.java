package code.sibyl.cache;


public interface Cache {

    Object get(Object key);

    void put(Object key, Object value);
}
