package model.ADTs;

import java.util.*;
public interface MyIStack<T> {
    T pop();
    void push(T element);

    boolean isEmpty();

    List<String> toListOfStrings();


}
