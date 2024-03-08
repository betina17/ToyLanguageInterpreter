package model.types;

import model.values.IValue;
import model.values.IntValue;

public class IntType implements IType{
    private static final int DEFAULT_VALUE = 0;
    public IntType() {
    }

    public boolean equals(Object another)
    {
        if(another instanceof  IntType)
            return true;
        else
            return false;

    }
    public String toString()
    {
        return "int";
    }
    @Override
    public IType deepcopy()
    {
        return new IntType();
    }

    @Override
    public IValue defaultValue()
    {
        return new IntValue(DEFAULT_VALUE);
    }
}
