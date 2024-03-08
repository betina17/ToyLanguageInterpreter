package model.values;
import model.types.*;
public class BoolValue implements IValue{
    boolean value;
    public BoolValue(boolean v)
    {
        value=v;
    }
    public boolean getValue()
    {
        return value;
    }
    public String toString()
    {
        return "boolean";
    }
    @Override
    public IType getType()
    {
        return new BoolType();
    }
    @Override
    public IValue deepcopy()
    {
        return new BoolValue(value);
    }

    @Override
    public boolean equals(Object another)
    {
        if(another instanceof BoolValue)
            return true;
        else
            return false;

    }
}
