package model.statements;

import model.ADTs.MyIDictionary;
import model.MyException;
import model.ProgramState;
import model.types.BoolType;
import model.types.IType;
import model.types.IntType;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;

public class VariableDeclarationStatement implements IStatement{
    String id;
    IType type; //cand declari o variabila, de ex int a, ea are un tip si un nume(id)

    public VariableDeclarationStatement(String id, IType type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException
    {
        MyIDictionary<String, IValue> symbolTable = state.getSymbolTable();
        if(symbolTable.lookUp(id)!=null)
            throw new MyException("The variable " + id + " is already declared!");

        symbolTable.update(id, type.defaultValue());
        return null;
    }

    public String toString()
    {
        return type.toString() + " " + id;
    }
    @Override
    public IStatement deepcopy()
    {
        return new VariableDeclarationStatement(id, type.deepcopy());
    }

    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String,IType> typeEnvironment) throws MyException
    {
        typeEnvironment.update(id, type);
        return  typeEnvironment;
    }
}
