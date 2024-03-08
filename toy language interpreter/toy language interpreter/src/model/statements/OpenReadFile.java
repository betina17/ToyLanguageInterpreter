package model.statements;
import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.ProgramState;
import model.MyException;
import model.expressions.IExpression;
import model.types.IType;
import model.types.StringType;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
public class OpenReadFile implements IStatement{
    private IExpression expression;

    public OpenReadFile(IExpression expression){
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIHeap heap = state.getHeap();
        IValue value = expression.evaluation(state.getSymbolTable(), heap);
        // Check if the evaluated expression is of String Type
        if (!value.getType().equals(new StringType()))
            throw new MyException("File name is not of string type.");
        String fileName = ((StringValue) value).getValue();
        // Check if the file name is already in the file table
        if (state.getFileTable().lookUp(fileName) != null)
            throw new MyException("File name is already in the file table.");

        // Try to open the file and put it in the file table if successful
        try{
            BufferedReader fileDescriptor = new BufferedReader(new FileReader(fileName));
            state.getFileTable().update(fileName, fileDescriptor);
        }
        catch (FileNotFoundException error){
            throw new MyException(error.getMessage());
        }
        return null;
    }
    @Override
    public IStatement deepcopy() {
        return new OpenReadFile(expression);
    }

    @Override
    public String toString() {
        return "openFileWithRead(" + expression.toString() + ")";
    }


    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String,IType> typeEnvironment) throws MyException
    {
        IType typeOfExpression = expression.typecheck(typeEnvironment);
        if(!typeOfExpression.equals(new StringType()))
            throw new MyException("OpenReadFileStatement exception: The name of the file is not of type String");
        return typeEnvironment;
    }
}