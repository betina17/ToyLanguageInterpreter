package model.expressions;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.MyException;
import model.types.BoolType;
import model.types.IType;
import model.types.IntType;
import model.values.BoolValue;
import model.values.IValue;

public class LogicExpression implements IExpression{
    IExpression expression1;
    IExpression expression2;
    String operation;

    public LogicExpression(IExpression expression1, IExpression expression2, String operation) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.operation = operation; //5-AND, 6-OR
    }
    @Override
    public IValue evaluation(MyIDictionary<String, IValue> symbolTable, MyIHeap heap) throws MyException
    {
        IValue value1 =expression1.evaluation(symbolTable, heap);  //evaluation returneaza rezultatul unei expresii de tip value=value sau
                                                                //variable=, sau de tip logic=0 sau 1, pt expresii logice
        if(value1.getType().equals(new BoolType()))            //sau de tip aritmetic, pt cele aritmetice=integer. ORICARE dintre aceste
        {
            IValue value2 = expression2.evaluation(symbolTable, heap);       //4 rezultate este de tip IValue, aceste rezultate sunt o valoare data de
            if(value2.getType().equals((new BoolType()))) {     //o expresie. de asta stocam expr1.eval in value1 de tip IValue.
                BoolValue boolean1 = (BoolValue) value1;      //daca te uiti in IExpression, vezi ca evaluation returneaza IValue
                BoolValue boolean2 = (BoolValue) value2;
                boolean number1, number2;
                number1 = boolean1.getValue();
                number2 = boolean2.getValue();
                if(operation.equals("&&"))
                    return new BoolValue(number1 && number2);  //true si false, sau true si true etc
                else if(operation.equals("||"))
                    return new BoolValue(number1 || number2);  //true sau false, etc
                else if (!operation.equals("&&") && !operation.equals("||") )
                    throw new MyException("The operation is not a valid or a logical one!");
            }
            else
                throw new MyException("The second operand is not a boolean one!");
        }
        else
            throw new MyException("The first operand is not a boolean one!");
        throw new MyException("Unexpected error during evaluation."); //in cazul in care nu a returnat nimic pana aici sau nu
    } //?                                                               //a aruncat nicio exceptie, tot trb sa returneze/arunce ceva
                                                                        //ca nu e void
    @Override
    public String toString() {
        return expression1.toString() + operation + expression2.toString() ;
    }
    @Override
    public IExpression deepcopy()
    {
        return new LogicExpression(expression1.deepcopy(), expression2.deepcopy(), operation);
    }
    public IType typecheck(MyIDictionary<String,IType> typeEnvironment) throws MyException
    {
        IType typeOfFirstExpression, typeOfSecondExpression;
        typeOfFirstExpression = expression1.typecheck(typeEnvironment);
        typeOfSecondExpression = expression2.typecheck(typeEnvironment);
        if(!typeOfFirstExpression.equals(new BoolType()))
            throw new MyException("First operand is not a boolean!");
        if(!typeOfSecondExpression.equals(new BoolType()))
            throw new MyException("Second operand is not a boolean!");
        return new BoolType();

    }
    //trebuie sa evaluam tipurile expresiilor care construiesc un logic expression

}
//expression1.deepcopy(). expression1 este declarat in clasa asta de tip IExpression, dar eu nu voi apela la executia programului
//o expresie de tip interfata, ci doar de tip clasa care implementeaza interfata, adica ArithExpr, LogicExpr, ValueExpr si ValueExpr
//de asta expression1.deepcopy() o sa apeleze functia deepcopy() din clasa din care face parte, sa zicem din ValueEXpression,
//daca am de exemplu expresia: 2 OR 3. e logic expr, cu expr1=2, care e direct valueExpr
//in care fac return new ValueExpr(IValue expression.deepcopy())-adica deepcopy la field-ul din ValueExpr.
//asta la randul lui apleaza deepcopy() din clasa care implementeaza IValue, adica IntValue sau BoolValue,
//unde fac deepcopy(){return new IntValue(int value)} si aici se opreste lantul, nu mai scriu int value.deepcopy() ca e int e primitiv
//pt ca pana la urma
//orice expresie returneaza uhn rezultat, pana la radacina, de tip int sau bool, adica primitiv, si se apeleaza deepcopy de la
//mare la mic, pana la radacina primitiva. valorile primitive sunt pe stack, nu pe heap, iar ele nu au nevoie de deepcopy().
//