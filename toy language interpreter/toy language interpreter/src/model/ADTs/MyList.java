package model.ADTs;
import java.util.*;
public class MyList<T> implements MyIList<T>{
    ArrayList<T> list ;

    public MyList(ArrayList<T> list) {
        this.list = list;
    }

    public MyList() {
        this.list = new ArrayList<>();
    }

    @Override
    public boolean addToOut(T element)
    {
        return list.add(element);
    }

    @Override
    public String toString() {
        return  list.toString();
    }

    @Override
    public List<String> getContentAsListOfStrings() {
        List<String> listOfStrings = new ArrayList<>();
        for (T element : list)
            listOfStrings.add(element.toString());
        return listOfStrings;
    }
}
