package textview;

import controller.Controller;
import model.ADTs.*;
import model.MyException;
import model.ProgramState;
import model.expressions.*;
import model.statements.*;
import model.types.*;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;
import repository.IRepository;
import repository.Repository;

import java.io.BufferedReader;

import model.values.StringValue;

public class Interpreter {

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

        public static void main(String[] all) {
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
        new IfStatement(new RelationalExpression(new ValueExpression(new IntValue(5)),new ValueExpression(new IntValue(4)),"<"), new AssignmentStatement("v", new ValueExpression(new IntValue(2))),
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
                    new CompoundStatement(new AssignmentStatement("varf", new ValueExpression(new StringValue("D:\\uni\\Second year\\MAP\\last lab\\lastLab\\src\\test.in"))),
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

        IStatement example5 = new CompoundStatement(new VariableDeclarationStatement("v",new ReferenceType(new IntType())),
            new CompoundStatement(new HeapAllocationStatement("v", new ValueExpression(new IntValue(20))),
                    new CompoundStatement(new VariableDeclarationStatement("a",new ReferenceType(new ReferenceType(new IntType()))),
                            new CompoundStatement(new HeapAllocationStatement("a", new VariableExpression("v")),
                                    new CompoundStatement(new PrintStatement(new VariableExpression("a")), new PrintStatement(new VariableExpression("v")))))));


        /*
        Ref int v;new(v,20);Ref Ref int a; new(v,10); new(v,30);print(rH(rH(a)))
        */
        IStatement example6 = new CompoundStatement(new VariableDeclarationStatement("v",new ReferenceType(new IntType())),
            new CompoundStatement(new HeapAllocationStatement("v", new ValueExpression(new IntValue(20))),
                    new CompoundStatement(new VariableDeclarationStatement("a",new ReferenceType(new ReferenceType(new IntType()))),
                            new CompoundStatement(new HeapAllocationStatement("a", new VariableExpression("v")),
                                    new CompoundStatement(
                                            new HeapAllocationStatement("v", new ValueExpression(new IntValue(30))), new PrintStatement(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a")))))))));

        //FORK EXAMPLE
        /*int v; Ref int a; v=10;new(a,22);
        fork(wH(a,30);v=32;print(v);print(rH(a)));
        print(v);print(rH(a))*/

       /* IStatement threadsExample = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
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
                                            new PrintStatement(new ReadHeapExpression(new VariableExpression("a"))))
                                    )
                            )
                    )
            )
        );*/

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

        //we typecheck each program before we try to execute them
        try{
                example1.typeCheck(new MyDictionary<>());
               //example2.typeCheck(new MyDictionary<>());
                //example3.typeCheck(new MyDictionary<>());
                programForFileOperationTesting.typeCheck(new MyDictionary<>());
                example5.typeCheck(new MyDictionary<>());
                example6.typeCheck(new MyDictionary<>());
                threadsExample.typeCheck(new MyDictionary<>());
        }
        catch(MyException e)
        {
                System.out.println(e.getMessage());
        }

        IRepository repository1 = new Repository();
        repository1.addProgramStateToRepository(createProgram(example1));
        Controller controller1 = new Controller(repository1);

        /*IRepository repository2 = new Repository();
        repository2.addProgramStateToRepository(createProgram(example2));
        Controller controller2 = new Controller(repository2);

        IRepository repository3 = new Repository();
        repository3.addProgramStateToRepository(createProgram(example3));
        Controller controller3 = new Controller(repository3);
*/
        IRepository repository4 = new Repository();
        repository4.addProgramStateToRepository(createProgram(programForFileOperationTesting));
        Controller controller4 = new Controller(repository4);

        IRepository repository5 = new Repository();
        repository5.addProgramStateToRepository(createProgram(example5));
        Controller controller5 = new Controller(repository5);

        IRepository repository6 = new Repository();
        repository6.addProgramStateToRepository(createProgram(example6));
        Controller controller6 = new Controller(repository6);

        IRepository repository7 = new Repository();
        repository7.addProgramStateToRepository(createProgram(threadsExample));
        Controller controller7 = new Controller(repository7);


        ViewTextMenu view = new ViewTextMenu();
        view.addCommand(new ExitCommand("0", "exit"));
        view.addCommand(new RunExampleCommand("1",example1.toString(),controller1));
        /*view.addCommand(new RunExampleCommand("2",example2.toString(),controller2));
        view.addCommand(new RunExampleCommand("3",example3.toString(),controller3));*/
        view.addCommand(new RunExampleCommand("4",programForFileOperationTesting.toString(),controller4));
        view.addCommand(new RunExampleCommand("5",example5.toString(),controller5));
        view.addCommand(new RunExampleCommand("6",example6.toString(),controller6));
        view.addCommand(new RunExampleCommand("7",example6.toString(),controller7));
        view.show();

        }
        }