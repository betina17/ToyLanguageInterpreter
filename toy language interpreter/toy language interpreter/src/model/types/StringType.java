package model.types;

import model.values.IValue;
import model.values.IntValue;
import model.values.StringValue;

public class StringType implements IType {
    private static final String DEFAULT_VALUE = "";

    public StringType() {
    }
    public boolean equals(Object another)
    {
        if(another instanceof  StringType)
            return true;
        else
            return false;

    }
    @Override
    public String toString()
    {
        return "int";
    }

    @Override
    public IType deepcopy()
    {
        return new StringType();
    }

    @Override
    public IValue defaultValue()
    {
        return new StringValue(DEFAULT_VALUE);
    }
}
