package model.ADTs;
import java.util.*;
public interface MyIDictionary<K,T> {

    Hashtable<K, T> getTable();
    void update(K key, T value); //add to dict
    T lookUp(K key);       //get from dict the value

    void remove(K key);

    Set<K> getKeys();

    Collection<T> getValues();

    MyIDictionary<K,T> deepCopy();

    Set<Map.Entry<K, T>> entrySet();
}

