package model;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.ADTs.MyIList;
import model.ADTs.MyIStack;
import model.statements.IStatement;
import model.values.IValue;

import java.io.BufferedReader;
import java.util.*;
public class ProgramState {
    MyIStack<IStatement> executionStack;
    MyIDictionary<String, IValue> symbolTable;
    MyIList<IValue> outputOfToyProgram;

    MyIDictionary<String, BufferedReader> fileTable;

    MyIHeap heap;
    IStatement originalProgram;

    private static int idCounter = 0; //this field will be seen by each prgstate, as it is static
    private final int id; //the personal and unique id of each prgstate
    //private static final Object idLock = new Object(); //this is a mutex (lock) and if a thread has aquired it, no other thread
    //can enter any synchronized block with the same lock until the first thread exits the block.
    //the synchronized block will be: synchronized(idLock){....}-this is a built-in synchronization method-more info at the end
    //of the class implementation
    //this lock will too be seen by all prgstates, as it should, as it is static


    public ProgramState(MyIStack<IStatement> executionStack, MyIDictionary<String, IValue> symbolTable,
                        MyIList<IValue> outputOfToyProgram, IStatement program, MyIDictionary<String, BufferedReader> fileTable,
                        MyIHeap heap) {
        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
        this.outputOfToyProgram = outputOfToyProgram;
        this.fileTable = fileTable;
        this.heap = heap;
        this.originalProgram = program.deepcopy();
        executionStack.push(program);
        this.id = getNextId();
    }

    public MyIStack<IStatement> getExecutionStack() {
        return executionStack;
    }

    public MyIDictionary<String, IValue> getSymbolTable() {
        return symbolTable;
    }

    public MyIList<IValue> getOutputOfToyProgram() {
        return outputOfToyProgram;
    }

    public MyIDictionary<String, BufferedReader> getFileTable() {
        return fileTable;
    }

    public MyIHeap getHeap() {
        return heap;
    }

    public void setHeap(MyIHeap newHeap)
    {
        this.heap = newHeap;
    }

    public String toString()
    {
       return "ID: " + String.valueOf(id) +"\n"+ "ExeStack: " + executionStack.toString() + "\n" + "SymTable: " + symbolTable.toString() +
               "\n" + "Out: " +outputOfToyProgram.toString() + "\n" + "FileTable: " + fileTable.getKeys() +
               "\n" + "HeapTable: " + heap.toString() + "\n" + "\n";
    }

    public Boolean isCompleted()
    {
        return executionStack.isEmpty();

    }

    public ProgramState executeOneStatementOfAProgram() throws MyException  //execute just the first statement
    {
        MyIStack<IStatement> stack = this.getExecutionStack();  //the program that we execute one step of is the program that we are in (we are in class ProgramState)
        if (stack.isEmpty())
            throw new MyException("the program state stack is empty");

        IStatement currentStatement = stack.pop();  //nu punem else pt ca daca arunca exceptia oricum nu ajunge aici
        return currentStatement.execute(this);

    }

    public static synchronized int getNextId()
    {

            return idCounter++; //it returns the current value of the counter, and then it increments it

    }

    public int getId()
    {
        return id;
    }
}
