package model.statements;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.ADTs.MyIStack;
import model.MyException;
import model.ProgramState;
import model.expressions.IExpression;
import model.types.BoolType;
import model.types.IType;
import model.values.BoolValue;
import model.values.IValue;

public class IfStatement implements IStatement {
    IExpression expression;
    IStatement thenStatement;
    IStatement elseStatement;

    public IfStatement(IExpression expression, IStatement thenStatement, IStatement elseStatement) {
        this.expression = expression;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException
    {
        MyIDictionary<String, IValue> symbolTable = state.getSymbolTable();
        MyIStack<IStatement> stack = state.getExecutionStack();
        MyIHeap heap = state.getHeap();
        IValue condition = expression.evaluation(symbolTable, heap);
        if(!condition.getType().equals(new BoolType()))
            throw new MyException("conditional expression is not a boolean!");
        else
        {
            BoolValue booleanCondition = (BoolValue) condition;
            if(booleanCondition.getValue() == true)
                stack.push(thenStatement);
            else
                stack.push(elseStatement);
        }
        return null;
    }

    public String toString()
    {
        return "IF " + expression.toString() + " THEN " + thenStatement.toString() + " ELSE " + elseStatement.toString();
    }
    @Override
    public IStatement deepcopy()
    {
        return new IfStatement(expression.deepcopy(), thenStatement.deepcopy(), elseStatement.deepcopy());
    }

    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String,IType> typeEnvironment) throws MyException
    {
        IType typeOfExpression = expression.typecheck(typeEnvironment);
        if(!typeOfExpression.equals(new BoolType()))
            throw new MyException("IfStatement exception: conditional expression is not a boolean!");
        thenStatement.typeCheck(typeEnvironment);
        elseStatement.typeCheck(typeEnvironment);
        return typeEnvironment;
    }


}
