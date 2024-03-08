package model.statements;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.ADTs.MyIStack;
import model.MyException;
import model.ProgramState;
import model.expressions.IExpression;
import model.values.IValue;
import model.types.IType;
public class AssignmentStatement implements IStatement {
    String id; //the variable to which we want to assign an expression
    IExpression expression; //the expression

    public AssignmentStatement(String id, IExpression expression) {
        this.id = id;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException
    {
        MyIStack<IStatement> stack = state.getExecutionStack();
        MyIDictionary<String, IValue> symbolTable = state.getSymbolTable();
        MyIHeap heap = state.getHeap();
        IValue variableInitialValue = symbolTable.lookUp(id);
        if(variableInitialValue!=null)
        {
            IValue expressionValue = expression.evaluation(symbolTable, heap); //luam valoarea expresiei
            IType expressionValueType = expressionValue.getType(); //luam tipul expresiei, getType() e methoda in IValue
            if(expressionValueType.equals(variableInitialValue.getType())) //daca variabila destinatie si valoarea asignata au acelasi tip
            {
                symbolTable.update(id, expressionValue);
            }
            else
                throw new MyException("Declared type of variable " + id + " does not match the type of the assigned expression!");
        }
        else
            throw new MyException("The variable " + id + "was not declared before!");

        return null;
    }
    public String toString()
    {
        return id+"="+expression.toString();
    }
    @Override
    public IStatement deepcopy()
    {
        return new AssignmentStatement(id, expression.deepcopy());
    }

    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String,IType> typeEnvironment) throws MyException
    {
       IType typeOfVariable = typeEnvironment.lookUp(id);
       //luam tipul variabilei din dictionar
        IType typeOfExpression = expression.typecheck(typeEnvironment);
        if(!typeOfExpression.equals(typeOfVariable))
            throw new MyException("AssignmentStatement exception: right hand side and left hand side are not of the same type!");
        return typeEnvironment;
    }
}
