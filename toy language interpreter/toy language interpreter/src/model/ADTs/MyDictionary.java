package model.ADTs;
import java.util.* ;
public class MyDictionary<K,T> implements MyIDictionary<K,T>{
    Hashtable<K,T> table;

    public Hashtable<K, T> getTable() {
        return table;
    }

    public MyDictionary(Hashtable<K, T> table) {
        this.table = table;
    }

    public MyDictionary() {
        this.table = new Hashtable<>();
    }

    //we also need empty constructors
    @Override
    public void update(K key, T value)
    {
        table.put(key, value);
    }
    @Override
    public T lookUp(K key){ return table.get(key);}

    @Override
    public String toString() {
        return table.toString();
    }

    public Set<K> getKeys()
    {
        return table.keySet();
    }

    @Override
    public void remove(K key)
    {
        table.remove(key);
    }

    @Override
    public Collection<T> getValues()
    {
        return table.values();
    }

    public MyIDictionary<K,T> deepCopy() {
        Hashtable<K, T> newTable = new Hashtable<>();
        for (Map.Entry<K, T> entry : table.entrySet()) {
            newTable.put(entry.getKey(), entry.getValue());
        }
        return new MyDictionary<K,T>(newTable);
    }
    //this creates a deepcopy. I have to do it manually, because the hashtable class does not have a deepcopy built-in method
    //and I also did not create one for it, as I did for an IStatement for example

    @Override
    public Set<Map.Entry<K, T>> entrySet()
    {
        return table.entrySet();
    }

}
