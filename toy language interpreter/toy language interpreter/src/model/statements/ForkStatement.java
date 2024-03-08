package model.statements;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIStack;
import model.ADTs.MyStack;
import model.MyException;
import model.ProgramState;
import model.types.IType;
import model.values.IValue;

import java.util.Stack;

public class ForkStatement implements IStatement{

    IStatement statementToBeForked;

    public ForkStatement(IStatement statementToBeForked) {
        this.statementToBeForked = statementToBeForked;
    }

    public ProgramState execute(ProgramState state) throws MyException
    {
        MyIStack<IStatement> stackWithForkedStatement = new MyStack<IStatement>(new Stack<IStatement>());
        MyIDictionary<String, IValue> symbolTable= state.getSymbolTable();
        MyIDictionary<String, IValue> symbolTableForForkedProgramState = symbolTable.deepCopy();
        ProgramState newForkedProgramState = new ProgramState(stackWithForkedStatement, symbolTableForForkedProgramState,
                state.getOutputOfToyProgram(), statementToBeForked, state.getFileTable(), state.getHeap());

        return newForkedProgramState;

    }
    public   IStatement deepcopy()
    {
        return new ForkStatement(statementToBeForked.deepcopy());
    }

    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String,IType> typeEnvironment) throws MyException
    {
        statementToBeForked.typeCheck(typeEnvironment);
        return typeEnvironment;
    }

}
