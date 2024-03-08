package model.types;

import model.values.IValue;
import model.values.IntValue;
import model.values.ReferenceValue;

//folosesti cand declari o variabila de tip referinta, adica Ref(TipulVariabilei)-bool/int/string/ref
//ex: Ref int a; Ref bool b; Ref string c; Ref Ref int d;
public class ReferenceType implements IType{
    private static final int DEFAULT_VALUE = 0;
    IType inner; //tipul valorii a din ref(a)
    public ReferenceType(IType inner) {
        this.inner = inner;
    }

    public boolean equals(Object another)
    {
        if(another instanceof  ReferenceType)
            return true;
        else
            return false;

    }
    public IType getInner()
    {
        return this.inner;
    }

    public String toString()
    {
        return "Ref(" + inner.toString() +")";
    }
    @Override
    public IType deepcopy()
    {
        return new ReferenceType(inner);
    }

    @Override
    public IValue defaultValue()
    {
        return new ReferenceValue(DEFAULT_VALUE, inner);
    }
}
