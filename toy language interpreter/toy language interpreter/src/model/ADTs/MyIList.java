package model.ADTs;

import java.util.List;

public interface MyIList<T> {
    boolean addToOut(T element) ;

    List<String> getContentAsListOfStrings();

}
