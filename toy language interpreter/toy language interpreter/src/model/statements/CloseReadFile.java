package model.statements;
import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.MyException;
import model.ProgramState;

import model.expressions.IExpression;
import model.types.IType;
import model.types.StringType;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseReadFile implements IStatement{
    private IExpression expression;

    public CloseReadFile(IExpression expression){
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIHeap heap = state.getHeap();
        IValue fileValue = expression.evaluation(state.getSymbolTable(), heap);
        // Check if the expression evaluates to a string type (we need a file name and that must be a string)
        if(!fileValue.getType().equals(new StringType()))
            throw  new MyException("File name " + fileValue + "is not of StringType");
        String fileName = ((StringValue) fileValue).getValue();
        // Check if the file is in the file table
        if (state.getFileTable().lookUp(fileName) == null)
            throw new MyException("File " + fileName + " has not been found in the File Table");
        // Get the file descriptor associated with the file name
        BufferedReader fileDescriptor = state.getFileTable().lookUp(fileName);
        // Check if there is a file descriptor associated with the computed file name
        if (fileDescriptor == null)
            throw new MyException("File " + fileName + " has no associated file descriptor");
        try{
            fileDescriptor.close();
        }
        catch (IOException error){
            throw new MyException(error.getMessage());
        }
        // Remove the file from the file table
        state.getFileTable().remove(fileName);
        return null;
    }

    @Override
    public IStatement deepcopy() {
        return new CloseReadFile(expression);
    }

    @Override
    public String toString() {
        return "closeFile(" + expression.toString() + ")";
    }


    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String,IType> typeEnvironment) throws MyException
    {
        IType typeOfExpression = expression.typecheck(typeEnvironment);
        if(!typeOfExpression.equals(new StringType()))
            throw new MyException("OpenReadFileStatement exception: The name of the file is not of type String");
        return typeEnvironment;
    }
}
