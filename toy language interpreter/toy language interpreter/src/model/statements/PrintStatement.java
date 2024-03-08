package model.statements;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.ADTs.MyIList;
import model.MyException;
import model.ProgramState;
import model.expressions.IExpression;
import model.types.IType;
import model.values.IValue;

public class PrintStatement implements IStatement {
    IExpression expression;

    public PrintStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException
    {
        MyIList<IValue> outputOfProgram = state.getOutputOfToyProgram();
        MyIDictionary<String, IValue> symbolTable = state.getSymbolTable();
        MyIHeap heap = state.getHeap();
        IValue value = expression.evaluation(symbolTable, heap);
        outputOfProgram.addToOut(value);
        return null;

    }
    public String toString()
    {
        return "print("+expression.toString()+")";
    }

    @Override
    public IStatement deepcopy()
    {
        return new PrintStatement(expression.deepcopy());
    }

    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String,IType> typeEnvironment) throws MyException
    {
        expression.typecheck(typeEnvironment);
        return typeEnvironment;
    }
    //facem typecheck la expresie.
}
