package model.ADTs;
import java.util.* ;
public class MyStack<T> implements  MyIStack<T>
{
    Stack<T> stack;

    public MyStack(Stack<T> stack) {
        this.stack = stack;
    }

    public MyStack() {
        this.stack = new Stack<>();
    }


    @Override
    public T pop()
    {
        return stack.pop();
    }
    @Override
    public void push(T element)
    {
        stack.push(element);
    }

    @Override
    public String toString() {
        return  stack.toString();
    }

    @Override
    public boolean isEmpty()
    {
        return stack.empty();
    }

    public List<String> toListOfStrings() {
        return stack.stream().map(Object::toString).toList();
    }




}
