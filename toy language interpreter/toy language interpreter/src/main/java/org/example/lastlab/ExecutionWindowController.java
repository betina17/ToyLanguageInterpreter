package org.example.lastlab;

import controller.Controller;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.Pair;
import model.ADTs.MyHeap;
import model.ADTs.MyIHeap;
import model.ADTs.MyIList;
import model.ADTs.MyList;
import model.MyException;
import model.ProgramState;


import model.values.IValue;
import model.values.StringValue;


import java.util.*;

public class ExecutionWindowController {
    private ProgramState previousProgramState;
    private Controller controller;
    public TableView<Pair<Integer, IValue>> heapTableView;
    public TableColumn<Pair<Integer, IValue>, Integer> heapAddress;
    public TableColumn<Pair<Integer, IValue>, String> heapValue;
    public ListView<String> outputListValue;
    public ListView<String> fileListView;
    public ListView<Integer> programStateListView;
    public ListView<String> executionStackListView;
    public TableView<Pair<String, IValue>> symbolTableView;
    public TableColumn<Pair<String, IValue>, String> symbolTableVariableName;
    public TableColumn<Pair<String, IValue>, String> symbolTableValue;
    public TextField numberOfProgramStates;
    public Button executeOneStep;

    public void initialize()
    {
        previousProgramState = null;
        heapAddress.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        heapValue.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));
        symbolTableVariableName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        symbolTableValue.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));
        executeOneStep.setOnAction(actionEvent ->{
            if(controller == null)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "The program was not selected", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }

            if(getCurrentProgram() == null || getCurrentProgram().getExecutionStack().isEmpty())
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Nothing Left To Execute", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
            
            try{
                previousProgramState = getCurrentProgram();
                controller.oneStepEachThreadWrapperGUI();
                populateViews();
            }
            catch(MyException error)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, error.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        });
        programStateListView.setOnMouseClicked(mouseEvent -> populateViews());
    }

    private ProgramState getCurrentProgram()
    {
        if(controller.getRepository().getListOfProgramStates().isEmpty())
            return previousProgramState;
        int indexOfChosenProgram = programStateListView.getSelectionModel().getSelectedIndex();
        if(indexOfChosenProgram < 0)
            indexOfChosenProgram = 0;
        if(indexOfChosenProgram >= controller.getRepository().getListOfProgramStates().size())
            indexOfChosenProgram = 0;
        return controller.getRepository().getListOfProgramStates().get(indexOfChosenProgram);
    }

    public void setController(Controller controller)
    {
        this.controller = controller;
        populateViews();
    }

    private void populateViews()
    {
        populateExecutionStackView();
        populateSymbolTableView();
        populateOutputView();
        populateFileTableView();
        populateProgramIDsView();
        populateHeap();
    }

    private void populateExecutionStackView()
    {
        ProgramState currentProgram = getCurrentProgram();
        List<String> executionStackAsStrings = new ArrayList<>();
        if(currentProgram != null)
            executionStackAsStrings = currentProgram.getExecutionStack().toListOfStrings().reversed();
        else if(previousProgramState != null)
            executionStackAsStrings = previousProgramState.getExecutionStack().toListOfStrings().reversed();
        executionStackListView.setItems(FXCollections.observableList(executionStackAsStrings));
        executionStackListView.refresh();
    }

    private void populateSymbolTableView()
    {
        ProgramState currentProgram = getCurrentProgram();
        List<Pair<String, IValue>> symbolTableList = new ArrayList<>();
        if(currentProgram != null)
            for(Map.Entry<String, IValue> entry: currentProgram.getSymbolTable().entrySet())
                symbolTableList.add(new Pair<>(entry.getKey(), entry.getValue()));
        else if(previousProgramState != null)
            for(Map.Entry<String, IValue> entry: previousProgramState.getSymbolTable().entrySet())
                symbolTableList.add(new Pair<>(entry.getKey(), entry.getValue()));
        symbolTableView.setItems(FXCollections.observableList(symbolTableList));
        symbolTableView.refresh();
    }

    private void populateOutputView()
    {
        List<String> ouptut = new ArrayList<>();
        if(!controller.getRepository().getListOfProgramStates().isEmpty())
            ouptut = controller.getRepository().getListOfProgramStates().get(0).getOutputOfToyProgram().getContentAsListOfStrings();
        else if(previousProgramState != null)
            ouptut = previousProgramState.getOutputOfToyProgram().getContentAsListOfStrings();
        outputListValue.setItems(FXCollections.observableList(ouptut));
        outputListValue.refresh();
    }

    private void populateFileTableView()
    {
       List<String> fileNames = new ArrayList<>();
        if(!controller.getRepository().getListOfProgramStates().isEmpty())
            fileNames = new ArrayList<String>(controller.getRepository().getListOfProgramStates().get(0).getFileTable().getKeys().stream().toList());
        else if(previousProgramState != null)
            fileNames = new ArrayList<String>(previousProgramState.getFileTable().getKeys().stream().toList());
        fileListView.setItems(FXCollections.observableArrayList(fileNames));
    }

    private void populateProgramIDsView()
    {
        List<ProgramState> listOfPrograms = controller.getRepository().getListOfProgramStates();
        List<Integer> idList = listOfPrograms.stream().map(ProgramState::getId).toList();
        programStateListView.setItems(FXCollections.observableList(idList));
        numberOfProgramStates.setText(Integer.toString(listOfPrograms.size()));
    }

    private void populateHeap()
    {
        MyIHeap heap = new MyHeap();
        if(!controller.getRepository().getListOfProgramStates().isEmpty())
            heap = controller.getRepository().getListOfProgramStates().get(0).getHeap();
        else if(previousProgramState != null)
            heap = previousProgramState.getHeap();
        List<Pair<Integer, IValue>> heapTableList = new ArrayList<Pair<Integer, IValue>>();
        for(var entry: heap.getContent().entrySet())
            heapTableList.add(new Pair<>(entry.getKey(), entry.getValue()));
        heapTableView.setItems(FXCollections.observableList(heapTableList));
        heapTableView.refresh();
    }
}
