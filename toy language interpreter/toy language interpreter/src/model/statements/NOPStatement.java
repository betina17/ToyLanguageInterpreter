package model.statements;

import model.ADTs.MyIDictionary;
import model.MyException;
import model.ProgramState;
import model.types.IType;

public class NOPStatement implements IStatement{
    public NOPStatement() {
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        return null;
    }

    @Override
    public IStatement deepcopy() {
        return new NOPStatement();
    }
    public String toString()
    {
        return "NOP";
    }
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String,IType> typeEnvironment) throws MyException
    {
        return typeEnvironment;
    }

}
