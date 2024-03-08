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

/*
Stack1={while (exp1) Stmt1 | Stmt2|...}
If exp1 is evaluated to BoolValue then
 If exp1 is evaluated to False then Stack2={Stmt2|...}
 Else Stack2={Stmt1 | while (exp1) Stmt1 | Stmt2|...}
Else
 throws new MyException("condition exp is not a boolean"
*/

public class WhileStatement implements IStatement{

    IExpression conditionalExpression;
    IStatement statementToExecuteInsideWhile;

    public WhileStatement(IExpression expression, IStatement statementToExecuteInsideWhile) {
        this.conditionalExpression = expression;
        this.statementToExecuteInsideWhile = statementToExecuteInsideWhile;
    }

    public ProgramState execute(ProgramState state) throws MyException
    {
        MyIStack<IStatement> stack = state.getExecutionStack();
        MyIDictionary<String, IValue> symbolTable = state.getSymbolTable();
        MyIHeap heap = state.getHeap();
        IValue valueOfExpression = conditionalExpression.evaluation(symbolTable, heap);
        if(!valueOfExpression.getType().equals(new BoolType()))
            throw new MyException("WhileStatement execution exception: The condition is not a boolean!");

        if(((BoolValue) valueOfExpression).getValue())
        {
            //push stmt1
            stack.push(statementToExecuteInsideWhile);
            //push the while statement again
            stack.push(this.deepcopy());
        }
        return null;
    }

    public IStatement deepcopy()
    {
        return new WhileStatement(conditionalExpression, statementToExecuteInsideWhile);
    }

    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String,IType> typeEnvironment) throws MyException
    {
        IType typeOfExpression = conditionalExpression.typecheck(typeEnvironment);

        if(!typeOfExpression.equals(new BoolType()))
            throw new MyException("WhileStatement typechecker exception: The condition is not a boolean!");
        statementToExecuteInsideWhile.typeCheck(typeEnvironment);
        return typeEnvironment;
    }

}
