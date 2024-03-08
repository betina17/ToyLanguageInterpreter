package org.example.lastlab;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import model.ADTs.*;
import model.MyException;
import model.ProgramState;
import model.expressions.*;
import model.statements.*;
import model.types.*;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;
import model.values.StringValue;
import repository.IRepository;
import repository.Repository;

import java.io.BufferedReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProgramStatesSelectionController implements Initializable {

    @FXML
    private ListView<String> programStatesListView;

    @FXML
    private Label chooseProgramLabel;

    //we also have to declare these by hand in the controller and put @FXML above because they are private and we want the fxml file
    //to have access to them

    private ObservableList<String> programStatesObservableList;

    private ArrayList<IStatement> programStates;
    //mai bine pun doar istatementurile din programState in lista, si creez un ProgramState cu toate adt-urile
    //doar pt programul selectat, pt eficienta de spatiu, ca mai bine stochez IStatement decat ProgramState

    private int selectedProgramState;

    private ProgramStateExecutionController executionController;

    @FXML
    private Button chooseProgramButton;



    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
         programStatesObservableList = FXCollections.observableArrayList();
         programStates = new ArrayList<>();
         this.populateProgramStatesLIstView();


    }
    //eu doar am declarat numele si tipul de date pt programStatesObservaleList si pt programStates,
    //iar aici le si initializez

    @FXML
    //nu dau param programStatesObservaleList si  programStates pt ca sunt stribute ale clasei si sunt vazute
    //toate metodele
    public void populateProgramStatesLIstView() {
        //1: int v; v=2; Print(v)
        IStatement example1 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(2))),
                        new CompoundStatement(new PrintStatement(new VariableExpression("v")), new NOPStatement())));


        //2: int a ; int b; a=2+3*5; b=a+1; Print(b)
        IStatement example2 = new CompoundStatement(new VariableDeclarationStatement("a", new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("b", new IntType()), new CompoundStatement(
                        new AssignmentStatement("a", new ArithmeticExpression(new ValueExpression(new IntValue(2)),
                                new ArithmeticExpression(new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5)), '*'), '+')),
                        new CompoundStatement(new AssignmentStatement("b", new ArithmeticExpression(new VariableExpression("a"),
                                new ValueExpression(new BoolValue(true)), '+')), new PrintStatement(new VariableExpression("b"))))));


        //3: bool a; int v; a=true; (if a then v=2 else v=3); print(v)
        IStatement example3 = new CompoundStatement(new VariableDeclarationStatement("a", new BoolType()),
                new CompoundStatement(new VariableDeclarationStatement("v", new IntType()), new CompoundStatement(
                        new AssignmentStatement("a", new ValueExpression(new BoolValue(true))), new CompoundStatement(
                        new IfStatement(new RelationalExpression(new ValueExpression(new IntValue(5)), new ValueExpression(new IntValue(4)), "<"), new AssignmentStatement("v", new ValueExpression(new IntValue(2))),
                                new AssignmentStatement("v", new ValueExpression(new IntValue(3)))), new PrintStatement(new VariableExpression("v"))))));

        /*
            Program 4:
            String varf;
            varf = "test.in";
            openRFile(varf);
            int varc;
            readFile(varf, varc);
            print(varc);

            readFile("src/logs/test2.in", varc);
            closeRFile(varf);
        */
        IStatement programForFileOperationTesting =
                new CompoundStatement(new VariableDeclarationStatement("varf", new StringType()),
                        new CompoundStatement(new AssignmentStatement("varf", new ValueExpression(new StringValue("D:\\uni\\Second year\\MAP\\a7\\lab3-toy language\\src\\test.in"))),
                                new CompoundStatement(new OpenReadFile(new VariableExpression("varf")),
                                        new CompoundStatement(new VariableDeclarationStatement("varc", new IntType()),
                                                new CompoundStatement(new ReadNumberFromFile(new VariableExpression("varf"), "varc"),
                                                        new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                                new CompoundStatement(new ReadNumberFromFile(new VariableExpression("varf"), "varc"),
                                                                        new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                                                new CloseReadFile(new VariableExpression("varf"))
                                                                        ))))))));

        /*
        Program 5:
        Ref int v;
        new(v,20);
        Ref Ref int a;
        new(a,v);
        print(v);
        print(a)
        */

        IStatement example5 = new CompoundStatement(new VariableDeclarationStatement("v", new ReferenceType(new IntType())),
                new CompoundStatement(new HeapAllocationStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new VariableDeclarationStatement("a", new ReferenceType(new ReferenceType(new IntType()))),
                                new CompoundStatement(new HeapAllocationStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(new PrintStatement(new VariableExpression("a")), new PrintStatement(new VariableExpression("v")))))));


        /*
        Ref int v;new(v,20);Ref Ref int a; new(v,10); new(v,30);print(rH(rH(a)))
        */
        IStatement example6 = new CompoundStatement(new VariableDeclarationStatement("v", new ReferenceType(new IntType())),
                new CompoundStatement(new HeapAllocationStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new VariableDeclarationStatement("a", new ReferenceType(new ReferenceType(new IntType()))),
                                new CompoundStatement(new HeapAllocationStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(
                                                new HeapAllocationStatement("v", new ValueExpression(new IntValue(30))), new PrintStatement(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a")))))))));

        //FORK EXAMPLE
        /*int v; Ref int a; v=10;new(a,22);
        fork(wH(a,30);v=32;print(v);print(rH(a)));
        print(v);print(rH(a))*/

        IStatement threadsExample = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("a", new ReferenceType(new IntType())),
                        new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(10))),
                                new CompoundStatement(new HeapAllocationStatement("a", new ValueExpression(new IntValue(22))),
                                        new CompoundStatement(new ForkStatement(
                                                new CompoundStatement(new WriteHeapStatement("a", new ValueExpression(new IntValue(30))),
                                                        new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(32))),
                                                                new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("a"))))
                                                        )

                                                )
                                        ), new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))))))));

        programStatesObservableList.add("1." + example1.toString());
        programStates.add(example1);
        //programStatesObservableList stores the string rep of the program states, si ea va fi afisata in listView
        //iar programStates stores the actual program state objects, adica e baza de date cum ar veni
        programStatesObservableList.add("2." + example2.toString());
        programStates.add(example2);

        programStatesObservableList.add("3." + example3.toString());
        programStates.add(example3);

        programStatesObservableList.add("4." + programForFileOperationTesting.toString());
        programStates.add(programForFileOperationTesting);

        programStatesObservableList.add("5." + example5.toString());
        programStates.add(example5);

        programStatesObservableList.add("6." + example6.toString());
        programStates.add(example6);

        programStatesObservableList.add("7." + threadsExample.toString());
        programStates.add(threadsExample);

        programStatesListView.setItems(programStatesObservableList);
    }

    public void checkIfProgramStateSelected()
    {
        int selectedProgramState = programStatesListView.getSelectionModel().getSelectedIndex();
        // the first item has an index of 0, the second item has an index of 1, and so on.
        try
        {
            IStatement statementToExecute = programStates.get(selectedProgramState);
            MyIDictionary<String, IType> typeEnvironment = new MyDictionary<>();
            statementToExecute.typeCheck(typeEnvironment);
            IRepository repository = new Repository();
            ProgramState programStateToExecute = createProgram(statementToExecute);
            repository.addProgramStateToRepository(programStateToExecute);
            Controller controller = new Controller(repository);
            executionController.setController(controller);


        }
        catch(MyException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString(), ButtonType.OK);
            alert.setTitle("Error Dialog!");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }

    private static ProgramState createProgram(IStatement exampleProgram)
    {
        MyIStack<IStatement> executionStack = new MyStack<>();
        //zicea ca pt ADTs sa facem generic interface si generic class, si stack-ul sa fie generic, deci toate 3 sunt generice
        MyIDictionary<String, IValue> symbolTable = new MyDictionary<>();
        MyIList<IValue> outputOfProgram = new MyList<>();
        MyIDictionary<String, BufferedReader> fileTable = new MyDictionary<>();
        MyIHeap heap = new MyHeap();
        return new ProgramState(executionStack, symbolTable, outputOfProgram, exampleProgram, fileTable, heap);
    }

    public void setExecutionController(ProgramStateExecutionController executionController)
    {
        this.executionController = executionController;
    }



}

/*
initialize(URL url, ResourceBundle resourceBundle)
URL url:

This parameter represents the location of the FXML file that was loaded to create the UI layout associated with this controller.
It can be used to determine the path or URL of the FXML file, which might be useful in certain situations, such as if you need to dynamically load additional resources based on the FXML file's location.
ResourceBundle resourceBundle:

This parameter is used for internationalization (i18n). A ResourceBundle contains locale-specific objects and can be used to load UI components' text and other values dynamically based on the user's locale.
This allows for the creation of multi-language applications by externalizing strings and other locale-sensitive elements to property files, which are then loaded by the ResourceBundle.
 */


/*ALERT
 catch(MyException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString(), ButtonType.OK);
            alert.setTitle("Error Dialog!");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }-explain this line by line

Certainly! The code snippet you've provided is a catch block that handles exceptions of type MyException. It creates and displays a JavaFX Alert dialog to inform the user about the error. Let's go through it line by line:

catch(MyException e):

This line starts the catch block. It catches exceptions of type MyException. When an exception of this type is thrown within the corresponding try block, the execution will jump to this catch block, and the caught exception will be stored in the variable e.
Alert alert = new Alert(Alert.AlertType.ERROR, e.toString(), ButtonType.OK);

An Alert object is created with three parameters:
Alert.AlertType.ERROR: This sets the type of the alert to an error message. It influences the icon and default behavior of the alert.
e.toString(): This converts the caught exception (e) to its string representation, which typically includes the name of the exception and its message. This string will be displayed as the content of the alert.
ButtonType.OK: This defines a button of type "OK" to be added to the alert. This gives the user a way to close the alert.
alert.setTitle("Error Dialog!");

This sets the title of the alert window to "Error Dialog!". The title is displayed in the window's title bar.
alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

This line adjusts the minimum height of the dialog pane. Region.USE_PREF_SIZE tells the pane to use its preferred size as its minimum size. This ensures that the alert dialog is not smaller than what is required to display its content properly.
alert.showAndWait();

This line displays the alert and waits for the user to respond. showAndWait() is a blocking call; it blocks further execution until the user closes the alert (e.g., by clicking the "OK" button).
Overall, this catch block is handling MyException by displaying a user-friendly error dialog. This approach is common in GUI applications, where you want to inform the user of errors in a more understandable and less intrusive way than, for instance, printing stack traces to the console.

 */