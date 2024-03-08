package controller;

import model.ADTs.MyHeap;
import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.ADTs.MyIStack;
import model.MyException;
import model.ProgramState;
import model.statements.IStatement;
import model.values.IValue;
import model.values.ReferenceValue;
import repository.IRepository;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller {
    IRepository repository;
    boolean displayFlag;

    private ExecutorService executor;

    public Controller(IRepository repository) {
        this.repository = repository;
        this.displayFlag = true;
    }

    public IRepository getRepository() {
        return repository;
    }

    public ProgramState getProgramById(int id) {
        for (ProgramState prg : repository.getListOfProgramStates()){
            if (prg.getId() == id)
                return prg;
        }
        return null;
    }



    /*public void executeAllStatementsOfAProgram() throws MyException {
        ProgramState currentProgram = repository.getCurrentProgramState();
        if (displayFlag)
            display(currentProgram);
        repository.logProgramStateExecution();

        while (!currentProgram.getExecutionStack().isEmpty()) {

            executeOneStatementOfAProgram(currentProgram);  //exception
            if (displayFlag)
                repository.logProgramStateExecution();
            unsafeGarbageCollector(currentProgram.getSymbolTable(), currentProgram.getHeap());
            display(currentProgram);
            repository.logProgramStateExecution();


        }
        if (!displayFlag)
            display(currentProgram);

    }*/

    public void allStep() throws MyException{
        executor = Executors.newFixedThreadPool(2);
//remove the completed programs
        List<ProgramState> prgList=removeCompletedPrograms(repository.getListOfProgramStates());
        while(prgList.size() > 0){
            oneStepForAllPrograms(prgList);
            prgList.forEach(program -> unsafeGarbageCollector(program.getSymbolTable(), program.getHeap()));

//remove the completed programs
            prgList=removeCompletedPrograms(repository.getListOfProgramStates());
        }
        executor.shutdownNow();
//HERE the repository still contains at least one Completed Prg
// and its List<PrgState> is not empty. Note that oneStepForAllPrg calls the method
//setPrgList of repository in order to change the repository
// update the repository state
        repository.setListOfProgramStates(prgList);
    }

    public void oneStepEachThreadWrapperGUI() throws MyException {
        executor = Executors.newFixedThreadPool(2);

        //remove the completed programs
        List<ProgramState> prgList = removeCompletedPrograms(repository.getListOfProgramStates());

        oneStepForAllPrograms(prgList);

        //remove again the completed programs
        prgList = removeCompletedPrograms(prgList);

        executor.shutdown();

        //set the repo to the new prgList, after the removal
        repository.setListOfProgramStates(prgList);
    }
    //asta e fix echivalenta cu allStep(), doar ca in loc sa tot execute cate un pas pt fiecare thread pana cand
    //tot programul e gata, cum face allStep() prin acel while, executa doar un singur pas pt fiecare thread
    //si atat. de asta functia asta o folosim in partea de gui, pt ca cu Run One Step BUtton fix asta vrem sa facem,
    //sa se parcurga doar un singur pas pt toate threadurile

    public void oneStepForAllPrograms(List<ProgramState> programList) throws MyException{
        // log the list of program states
        for (ProgramState program : programList) {
            repository.logProgramStateExecution(program);
        }
        // Prepare the list of callables for concurrency
        List<Callable<ProgramState>> callList = programList.stream()
                .map((ProgramState program) -> (Callable<ProgramState>) program::executeOneStatementOfAProgram).toList();
        // Start the execution of callables
        try {
            // This list contains the newly created threads from the already running programs
            List<ProgramState> newProgramList = executor.invokeAll(callList).stream().map(future -> {
                try {
                    return future.get();
                } catch (java.util.concurrent.ExecutionException | InterruptedException exception) {
                    repository.logErrorMessage(exception.getMessage());
                    System.exit(1);
                }
                return null;
            }).filter(Objects::nonNull).toList();
            // Add all the new programs to the programs list
            programList.addAll(newProgramList);
            // After they have been executed and garbage collected, log them
            programList.forEach(program -> unsafeGarbageCollector(program.getSymbolTable(), program.getHeap()));
            logPrograms(programList);
            programList.forEach(program -> display(program));
            // Now save them to the repository
            repository.setListOfProgramStates(programList);
        } catch (InterruptedException  e) {
            repository.logErrorMessage(e.getMessage());
            System.exit(1);
        }

        }
    public void logPrograms(List<ProgramState> programs) throws MyException
    {

            programs.forEach(program ->
            {
                try {
                    repository.logProgramStateExecution(program);
                } catch (MyException e) {
                    System.out.println(e.getMessage());
                }
            });
    }

    public void unsafeGarbageCollector(MyIDictionary<String, IValue> symbolTable, MyIHeap heap) {
        //Create a list with all the addresses that appear in the symbolTable, as well as in the heap(when at one location in the heap
        //you have instead of an int or bool another RefValue, which holds an address and a locationType, or, again another RefValue
        //we concatenate the addresses from the symTable and from the heap, even if we will have duplicates
        List<Integer> addressesToKeep = Stream.concat(getAddresses(symbolTable.getValues()).stream(),
                getAddresses(heap.getValues()).stream()).toList();
        //Stream.concat() concatenates 2 streams. symbolTable.getValues() returns Collection<IValue> values,
        //getAddresses(symbolTable.getValues() returns List<Integer>, which we the transform into a stream, and concat it
        //to the other stream, from the heap

        //heap.entrySet() returns a Set of key-value pairs from the map heap. Each element in this set is a Map.Entry object
        // representing a key-value pair. we create a stream from the set of key-value pairs in the heap
        //trebuie sa filtram heap-ul astfel incat sa ramana doar perechile la care cheia se regaseste in lista concatenata
        //de adrese prezente in symTable si in heap

        Map<Integer, IValue> newMap =  heap.getContent().entrySet().stream().filter(elem -> addressesToKeep.contains(elem.getKey())).collect(
                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        heap.setContent(newMap);



    }

    public List<Integer> getAddresses(Collection<IValue> values) {
        //we give this function the RefValues from SymTable and heap, and it returns a list of only the addresses from those values
        return values.stream()
                .filter(elem -> elem instanceof ReferenceValue)
                .map(elem -> ((ReferenceValue) elem).getAddress())
                .collect(Collectors.toList());
    }

    public void display(ProgramState programState) {
        System.out.println(programState.toString());
    }

    public List<ProgramState> removeCompletedPrograms(List<ProgramState> programList) {
        return programList.stream().filter(program -> !program.isCompleted()).collect(Collectors.toList());
    }
    //returns list of programs which are not completed yet
    /*
    Collecting into a Map: Finally, the .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)) part collects the
    filtered entries into a new map. Here's what each component does:
Collectors.toMap(): This is a collector that accumulates elements into a Map.
Map.Entry::getKey: For each Map.Entry in the stream, this method reference is used to retrieve the key. This key will be
used as the key in the new map.
Map.Entry::getValue: For each Map.Entry in the stream, this method reference is used to retrieve the value. This value will
be associated with its corresponding key in the new map.
     */


}
