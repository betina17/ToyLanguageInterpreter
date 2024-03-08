package model.values;

import model.types.*;

public class IntValue implements IValue{
    int value;
    public IntValue(int v)
    {
        value=v;
    }
    public int getValue() {
        return value;
    }
    public String toString()
    {
        return Integer.toString(value);
    }
    @Override
    public IType getType() {
        return new IntType();
    }
    public int getVal()
    {
        return value;
    }
    @Override
    public IValue deepcopy()
    {
        return new IntValue(value);
    }

    @Override
    public boolean equals(Object another)
    {
        if(another instanceof  IntValue)
            return true;
        else
            return false;

    }

}
