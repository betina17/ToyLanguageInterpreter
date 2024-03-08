package model.ADTs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import model.values.IValue;
public class MyHeap implements MyIHeap{
    private Map<Integer,IValue> heap ;

    //I have to use classes for the generic type parameters, I cannot use primitives, so I cannot use int, I have to use integer
    private int lastAllocatedHeapLocation;
    public MyHeap(Map<Integer, IValue> heap) {
        this.heap = heap;
        this.lastAllocatedHeapLocation = 0;
    }

    public MyHeap() {
        this.heap = new HashMap<>();
        this.lastAllocatedHeapLocation = 0;
    }

    public void setLastAllocatedHeapLocation(int newLocation)
    {
        this.lastAllocatedHeapLocation = newLocation;
    }
    @Override
    public int getLastAllocatedLocation()
    {
        return lastAllocatedHeapLocation;
    }
    @Override
    public int update(IValue value)
    {
        int key = getLastAllocatedLocation();
        key = key+1;
        setLastAllocatedHeapLocation(key);
        heap.put(key, value);
        return key;

    }

    @Override
    public void changeValueFromAnAddress(int key, IValue value)
    {
        heap.put(key, value);
    }
    @Override
    public IValue lookUp(int key){ return heap.get(key);}

    @Override
    public String toString() {
        return heap.toString();
    }

    public Set<Integer> getKeys()
    {
        return heap.keySet();
    }

    public boolean containsKey(int key)
    {
        return heap.keySet().contains(key);
    }

    @Override
    public void remove(int key)
    {
        heap.remove(key);
    }

    public Collection<IValue> getValues()
    {
        return heap.values();
    }


    public Map<Integer, IValue> getContent()
    {
        return heap;
    }

    public void setContent(Map<Integer, IValue> newMap)
    {
        this.heap = newMap;
    }


}
