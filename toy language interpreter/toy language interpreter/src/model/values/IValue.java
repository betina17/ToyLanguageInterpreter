package model.values;
import model.types.*;
public interface IValue {
    IType getType();
    IValue deepcopy();


}
