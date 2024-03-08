package model.statements;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.MyException;
import model.ProgramState;

import model.expressions.IExpression;
import model.types.IType;
import model.types.IntType;
import model.types.StringType;
import model.values.IValue;
import model.values.IntValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadNumberFromFile implements IStatement {
    private IExpression expression;

    private String varName;

    public ReadNumberFromFile(IExpression expression, String varName){
        this.expression = expression;
        this.varName = varName;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        // Check if the variable is in the symbol table
        if (state.getSymbolTable().lookUp(varName) == null)
            throw new MyException("Variable " + varName + " has not been declared");
        // Check if variable is of type IntType
        if (!state.getSymbolTable().lookUp(varName).getType().equals(new IntType()))
            throw new MyException("Variable " + varName + " is not of IntType");
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
        // Try to read a line from the file and translate it into an int (if it's a null then the value is 0)
        try{
            String line = fileDescriptor.readLine();
            IntValue lineValue;
            if (line == null)
                lineValue = new IntValue(0);
            else{
                // parseInt method throws NumberFormatException if the line that has been read does not respect the format of an int
                try{
                    lineValue = new IntValue(Integer.parseInt(line));
                }
                catch (NumberFormatException error){
                    throw new MyException("Invalid number format");
                }
            }
            // Update the variable with the new value read from the file
            state.getSymbolTable().update(varName, lineValue);
        }
        catch (IOException error){
            throw new MyException(error.getMessage());
        }
        return null;
    }

    @Override
    public IStatement deepcopy() {
        return new ReadNumberFromFile(expression, varName);
    }

    @Override
    public String toString() {
        return "readNumber(" + expression.toString() + ", " + varName + ")";
    }


    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String,IType> typeEnvironment) throws MyException
    {
        IType typeOfVariable  = typeEnvironment.lookUp(varName);
        if(!typeOfVariable.equals(new IntType()))
            throw new MyException("ReadNumberFromFileStatement typecheck exception: The variable you are trying to read into" +
                    "is not of type int");
        IType typeOfExpression  = expression.typecheck(typeEnvironment);
        if(!typeOfExpression.equals(new StringType()))
            throw new MyException("ReadNumberFromFileStatement typecheck exception: The variable you are trying to read into" +
                    "is not of type int");
        return typeEnvironment;

    }
}
