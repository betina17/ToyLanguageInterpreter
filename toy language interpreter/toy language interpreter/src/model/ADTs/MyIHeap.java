package model.ADTs;

import model.values.IValue;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface MyIHeap {
    int update(IValue value); //add to dict

    IValue lookUp(int key);       //get from dict

    void remove(int key);

    Set<Integer> getKeys();

    public boolean containsKey(int key);
    int getLastAllocatedLocation();


    void changeValueFromAnAddress(int key, IValue value);

    Collection<IValue> getValues();

    Map<Integer, IValue> getContent();

    void setContent(Map<Integer, IValue> newHeap);

    void setLastAllocatedHeapLocation(int newLocation);



    //ai grija ca toate fct pe care le folosesti in clasa MyHeap sa iti fie declarate in interfata, pt ca in program eu lucrez
    //doar cu interfata, si daca in interfata nu imi vede declarata o fct din clasa care da inherit la interfata, pe care eu
    //vreau sa o folosesc, atunci nu o sa pot sa o folosesc.
}
