package model.values;

import model.types.IType;
import model.types.IntType;
import model.types.StringType;

public class StringValue implements IValue{
    String value;
    public StringValue(String v)
    {
        value=v;
    }
    public String getValue() {
        return value;
    }
    public String toString()
    {
        return value;
    }
    @Override
    public IType getType() {
        return new StringType();
    }
    public String getVal()
    {
        return value;
    }
    @Override
    public IValue deepcopy()
    {
        return new StringValue(value);
    }

    @Override
    public boolean equals(Object another)
    {
        if(another instanceof  StringValue)
            return true;
        else
            return false;

    }
}
