package model.expressions;
import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.MyException;
import model.types.IType;
import model.values.IValue;
public interface IExpression {
    IValue  evaluation(MyIDictionary<String, IValue> symbolTable, MyIHeap heap) throws MyException;
    IExpression deepcopy();
    IType typecheck(MyIDictionary<String,IType> typeEnvironment) throws MyException;
    //ca sa intelegi de ce implementezi asa functia typecheck uita-te la evaluation
    //trebuie sa returnezi tipul fiecarei expresii
    //populezi dictionarul in VariableDeclarationStatement, acolo adaugi varibilele
}
