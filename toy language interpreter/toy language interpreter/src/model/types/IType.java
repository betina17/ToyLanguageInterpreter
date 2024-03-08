package model.types;
import model.values.IValue;
public interface IType {
    IType deepcopy();
    IValue defaultValue();
}
