package model.expressions;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.MyException;
import model.types.BoolType;
import model.types.IType;
import model.types.IntType;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;

public class RelationalExpression implements IExpression{
    IExpression expression1;
    IExpression expression2;
    String operation; //1-plus, 2-minus, 3-star, 4-divide

    public RelationalExpression(IExpression expression1, IExpression expression2, String operation) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.operation = operation;
    }

    @Override
    public IValue evaluation(MyIDictionary<String, IValue> symbolTable, MyIHeap heap) throws MyException {
        IValue value1, value2;
        value1 = expression1.evaluation(symbolTable, heap);
        if (value1.getType().equals(new IntType())) {
            value2 = expression2.evaluation(symbolTable, heap);
            if (value2.getType().equals(new IntType())) {
                IntValue integer1 = (IntValue) value1;
                IntValue integer2 = (IntValue) value2;
                int number1, number2;
                number1 = integer1.getVal();
                number2 = integer2.getVal();
                if (operation.equals("<"))
                {
                    return new BoolValue(number1 < number2);
                }
                else if (operation.equals("<="))
                {
                    if(number1 <= number2)
                        return new BoolValue(true);
                    else
                        return new BoolValue(false);
                }
                else if (operation.equals("=="))
                {
                    if(number1 == number2)
                        return new BoolValue(true);
                    else
                        return new BoolValue(false);
                }
                else if (operation.equals("!=")) {

                    if(number1 != number2)
                        return new BoolValue(true);
                    else
                        return new BoolValue(false);

                }
                else if (operation.equals(">")) {

                    if(number1 > number2)
                        return new BoolValue(true);
                    else
                        return new BoolValue(false);

                }
                else if (operation.equals(">=")) {

                    if(number1 >= number2)
                        return new BoolValue(true);
                    else
                        return new BoolValue(false);

                }

                throw new MyException("The operation provided is not a valid or a relational one");

            } else
                throw new MyException("second operand is not an integer!");
        } else
            throw new MyException("first operand is not an integer");

    }


    public String toString() {
        return expression1.toString() + operation + expression2.toString();
    }

    @Override
    public IExpression deepcopy() {
        return new RelationalExpression(expression1.deepcopy(), expression2.deepcopy(), operation);
    }

    public IType typecheck(MyIDictionary<String,IType> typeEnvironment) throws MyException
    {
        IType typeOfFirstExpression, typeOfSecondExpression;
        typeOfFirstExpression = expression1.typecheck(typeEnvironment);
        typeOfSecondExpression = expression2.typecheck(typeEnvironment);
        if(!typeOfFirstExpression.equals(new IntType()))
            throw new MyException("First operand is not an integer!");
        if(!typeOfSecondExpression.equals(new IntType()))
            throw new MyException("Second operand is not an integer!");
        return new IntType();

    }
}
