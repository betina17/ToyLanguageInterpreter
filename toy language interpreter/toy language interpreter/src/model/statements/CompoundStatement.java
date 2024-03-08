package model.statements;

import model.MyException;
import model.ADTs.*;
import model.ProgramState;
import model.types.IType;

public class CompoundStatement implements IStatement {
    IStatement firstStatement;
    IStatement secondStatement;

    public CompoundStatement(IStatement firstStatement, IStatement secondStatement) {
        this.firstStatement = firstStatement;
        this.secondStatement = secondStatement;
    }

    public String toString(){
        return "(" + firstStatement.toString() + ";" + secondStatement.toString() + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIStack<IStatement> stack = state.getExecutionStack();
        stack.push(secondStatement);
        stack.push(firstStatement);
        return null;
    }
    @Override
    public IStatement deepcopy()
    {
        return new CompoundStatement(firstStatement.deepcopy(), secondStatement.deepcopy());
    }

    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String,IType> typeEnvironment) throws MyException
    {
        return secondStatement.typeCheck(firstStatement.typeCheck(typeEnvironment));
    }
    //trebuie sa faca typecheck la fiecare statement. dar de ce ii da la typecheck de la primu stmt typeenv de la typechecku
    //celui de-al doilea?
}
