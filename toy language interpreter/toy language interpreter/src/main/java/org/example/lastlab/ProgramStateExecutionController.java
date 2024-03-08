package org.example.lastlab;

import controller.Controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.util.Pair;
import model.ADTs.MyDictionary;
import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.ADTs.MyIStack;
import model.MyException;
import model.ProgramState;
import model.statements.IStatement;
import model.values.IValue;

import java.net.URL;
import java.util.*;

public class ProgramStateExecutionController implements Initializable {

    @FXML
    private Label HeapLabel;

    @FXML
    private TableView<Pair<String, String>> HeapTableView;

    @FXML
    private TableColumn<Pair<String, String>, String> AddressTableColumn;

    @FXML
    private TableColumn<Pair<String, String>, String> ValueTableColumn;

    //---------OUT----------------------------
    @FXML
    private Label OutLabel;

    @FXML
    private ListView<String> OutListView;

    //---------SYM TABLE----------------------------
    @FXML
    private Label SymTableLabel;

    @FXML
    private TableView<Pair<String, String>> SymTableTableView;

    @FXML
    private TableColumn<Pair<String, String>, String> VariableNameTableColumn;

    @FXML
    private TableColumn<Pair<String, String>, String> ValueSymTblTableColumn;

    //---------EXE STACK----------------------------
    @FXML
    private Label ExeStackLabel;

    @FXML
    private ListView<String> ExeStackListView;

    //---------FILE TABLE----------------------------
    @FXML
    private Label FileTableLabel;

    @FXML
    private ListView<String> FileTableListView;

    //---------PRG STATES----------------------------

    @FXML
    private Label PrgStateIdentifiersLabel;

    @FXML
    private ListView<String> PrgStateIdentifiersListView;



    @FXML
    private TextField NoOfPrgStatesTextField;

    @FXML
    private Button RunOneStepButton;

    @FXML
    private Label nrOfPrgStatesLabel;


    private Controller controller;

    private ProgramState stateToExecute;
   // private ProgramState previousStateExecuted;

    //-------------------------------------------------------------------------

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //previousStateExecuted = controller.getRepository().getListOfProgramStates().get(0);
        this.NoOfPrgStatesTextField.setText(String.valueOf(this.controller.getRepository().getListOfProgramStates().size()));
        this.stateToExecute = null;

        AddressTableColumn.setCellValueFactory(p ->
        {
            return new SimpleStringProperty(p.getValue().getKey());
        });
        ValueTableColumn.setCellValueFactory(p ->
        {
            return new SimpleStringProperty(p.getValue().getValue());
        });
        VariableNameTableColumn.setCellValueFactory(p ->
        {
            return new SimpleStringProperty(p.getValue().getKey());
        });
        ValueSymTblTableColumn.setCellValueFactory(p ->
        {
            return new SimpleStringProperty(p.getValue().getValue());
        });


    }

    public void setController(Controller controller)
    {
        this.controller = controller;
        populateViews();

    }

    public void OneStepButtonClicked()
    {
        if (this.controller == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "PrgState was not selected from the other window!!!", ButtonType.OK);
            alert.setTitle("Error Dialog!");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }

        else {
            if (this.stateToExecute.getExecutionStack().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The stack is empty!Nothing left to execute!!", ButtonType.OK);
                alert.setTitle("Error Dialog!");
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.showAndWait();
            }
            else
            {
                try
                {
                    //this.previousStateExecuted = getPrg();
                    controller.oneStepEachThreadWrapperGUI();
                    populateViews();

                }catch (MyException exception) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, exception.toString(), ButtonType.OK);
                    alert.setTitle("Error Dialog!");
                    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    alert.showAndWait();
                }
            }
        }

    }

    public void populateViews()
    {
        populateExeStackListView();
        populateOutListView();
        populateFileTableListView();
        populateSymTableTableView();
        populateHeapTableView();
        populateProgramIDsView();
    }

    private ProgramState getPrg()
    {
        String id = PrgStateIdentifiersListView.getSelectionModel().getSelectedItem();
        return (controller.getProgramById(Integer.parseInt(id)) == null) ?  controller.getRepository().getListOfProgramStates().get(0): controller.getProgramById(Integer.parseInt(id));

    }
    //daca thread-ul curent selectat e null, se va returna primul thread din lista, thread-ul parinte
    //daca nu,se returneaza thread-ul cu id-ul selectat

    private void populateProgramIDsView() {
        List<ProgramState> listOfPrograms = controller.getRepository().getListOfProgramStates();
        List<String> idList = listOfPrograms.stream().map(ProgramState::getId).map(n->Integer.toString(n)).toList();
        PrgStateIdentifiersListView.setItems(FXCollections.observableList(idList));
        nrOfPrgStatesLabel.setText(Integer.toString(listOfPrograms.size()));
    }

    public void populateExeStackListView()
    {
       List<String> statementList = this.getPrg().getExecutionStack().toListOfStrings();
        ExeStackListView.setItems(FXCollections.observableList(statementList));
    }

    public void populateOutListView()
    {
        List<String> outList = Collections.singletonList(this.getPrg().getOutputOfToyProgram().toString());
        OutListView.setItems(FXCollections.observableList(outList));
    }

    public void populateFileTableListView()
    {
        List<String> fileNames = new ArrayList<>();
        getPrg().getFileTable().getTable().forEach((key, value) ->{
            fileNames.add(key);
        });
        FileTableListView.setItems(FXCollections.observableList(fileNames));
    }

    public void populateSymTableTableView()
    {
        MyIDictionary<String, IValue> symbolTable = getPrg().getSymbolTable();
        // Populate the symbol table data into a list of Pair
        List<Pair<String, IValue>> symbolTableList = new ArrayList<>();
        for (var entry : symbolTable.getTable().entrySet())
            symbolTableList.add(new Pair<>(entry.getKey(), entry.getValue()));

        List<Pair<String, String>> symbolTableStringList = new ArrayList<>();
        for (var entry : symbolTableList)
            symbolTableStringList.add(new Pair<>(entry.getKey(), entry.getValue().toString()));

        SymTableTableView.setItems(FXCollections.observableList(symbolTableStringList));
        SymTableTableView.refresh();
    }

    public void populateHeapTableView()
    {

        MyIHeap heap = getPrg().getHeap();
        // Populate the heap data into a list of Pair
        List<Pair<Integer, IValue>> heapTableList = new ArrayList<>();
        for (var entry : heap.getContent().entrySet())
            heapTableList.add(new Pair<>(entry.getKey(), entry.getValue()));

        List<Pair<String, String>> heapTableStringList = new ArrayList<>();
        for (var entry : heapTableList)
            heapTableStringList.add(new Pair<>(entry.getKey().toString(), entry.getValue().toString()));

        HeapTableView.setItems(FXCollections.observableList(heapTableStringList));
        HeapTableView.refresh();
    }




}
