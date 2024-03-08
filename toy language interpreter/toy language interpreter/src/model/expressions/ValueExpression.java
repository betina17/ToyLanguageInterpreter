package model.expressions;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.MyException;
import model.types.IType;
import model.values.IValue;

public class ValueExpression implements IExpression{
    IValue expression;

    public ValueExpression(IValue expression) {
        this.expression = expression;
    }

    @Override
    public IValue evaluation(MyIDictionary<String, IValue> symbolTable, MyIHeap heap) throws MyException
    {
        return expression; //Value evaluation: Eval(Number) returneaza Number
    }
    public String toString()
    {
        return expression.toString(); //returneaza valoarea
    }


    @Override
    public IExpression deepcopy() {
        return new ValueExpression(expression.deepcopy());
    }

    public IType typecheck(MyIDictionary<String,IType> typeEnvironment) throws MyException
    {
        return expression.getType();
    }

}
