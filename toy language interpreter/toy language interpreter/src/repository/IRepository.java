package repository;

import model.MyException;
import model.ProgramState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public interface IRepository {

    boolean addProgramStateToRepository(ProgramState programState);
    List<ProgramState> getListOfProgramStates();

    void setListOfProgramStates(List<ProgramState> newListOfProgramStates);

    void logProgramStateExecution(ProgramState programState) throws MyException;

    void setLogPathFile(String path);

    void logErrorMessage(String errorMsg);





}
