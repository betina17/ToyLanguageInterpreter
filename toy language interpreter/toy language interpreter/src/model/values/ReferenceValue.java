package model.values;
import model.types.IType;
import model.types.IntType;
import model.types.ReferenceType;
//folosesti cand declari dai valoare unei variabile de tip referinta. ex: newRef(10, new IntType())
public class ReferenceValue implements IValue{
    int address;
    IType locationType; //tipul de data de la locatia de memorie (adresa) respectiva

    public ReferenceValue(int address, IType locationType) {
        this.address = address;
        this.locationType = locationType;
    }

    public int getAddress() {
        return address;
    }
    public String toString()
    {
        return "(" +String.valueOf(address)  + "," + locationType.toString() +  ")";
    }
    @Override
    public IType getType() {
        return new ReferenceType(locationType);
    }

    public IType getLocationType()
    {
        return locationType;
    }

    @Override
    public IValue deepcopy()
    {
        return new ReferenceValue(address, locationType);
    }

    @Override
    public boolean equals(Object another)
    {
        if(another instanceof  ReferenceValue)
            return true;
        else
            return false;

    }
}
