package repository;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIList;
import model.ADTs.MyIStack;
import model.MyException;
import model.ProgramState;
import model.statements.IStatement;
import model.values.IValue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository{
    List<ProgramState> listOfProgramStates;

    String logPathFile;

    public Repository() {
        this.listOfProgramStates = new ArrayList<>();

        this.logPathFile ="D:\\uni\\Second year\\MAP\\last lab\\lastLab\\src\\log.txt";
    }
    //"D:\\uni\\Second year\\MAP\\lab3\\lab3-toy language\\src\\log.txt";

    public List<ProgramState> getListOfProgramStates() {
        return listOfProgramStates;
    }


   public void setListOfProgramStates(List<ProgramState> newListOfProgramStates)
   {
       this.listOfProgramStates = newListOfProgramStates;
   }

    public boolean addProgramStateToRepository(ProgramState programState)
    {
        return listOfProgramStates.add(programState);
    }

    public void setLogPathFile(String logPathFile) {
        this.logPathFile = logPathFile;
    }



    public String getLogPathFile() {
        return logPathFile;
    }



    public String toString()
    {
        String programStatesAsString = "";
        for(ProgramState programState: listOfProgramStates)
        {
            programStatesAsString = programStatesAsString + programState.toString();
        }
        return programStatesAsString;
    }

    public void logProgramStateExecution(ProgramState programToLog) throws MyException
    {

        try {
            PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logPathFile, true)));
            logFile.println(programToLog.toString());
            logFile.close();
        }
        catch(IOException e)
        {
            throw new MyException("There was an error in opening  the log file");
        }

    }

    public void logErrorMessage(String errorMsg) {
        try {
            PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logPathFile, false)));
            logFile.println(errorMsg + '\n');
            logFile.close();
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
    }

}
