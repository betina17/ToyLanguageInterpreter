package model.statements;
import model.expressions.IExpression;
import model.ProgramState;
import model.MyException;
import model.types.IType;
import model.values.IValue;
import model.values.ReferenceValue;
import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.types.ReferenceType;
//TODO: Heap Writing: Define and integrate the following statement
//wH(Var_Name, expression)
//• it is a statement which takes a variable and an expression, the variable contains the heap address, the expression
// represents the new value that is going to be stored into the heap, that is already allocated with another value
//• first we check if var_name is a variable defined in SymTable, if its type is a Ref type and if the address from the
// RefValue associated in SymTable is a key in Heap. If not, the execution is stopped with an appropriate error message.
//• Second the expression is evaluated and the result must have its type equal to the locationType of the var_name type.
// If not, the execution is stopped with an appropriate message.
//• Third we access the Heap using the address from var_name and that Heap entry is updated to the result of the
// expression evaluation.
//and contains something
//Example: Ref int v;new(v,20);print(rH(v)); wH(v,30);print(rH(v)+5);
//At the end of execution: Heap={1->30}, SymTable={v->(1,int)} and Out={20, 35}
//adica la adresa unde era alocata valoarea initiala, 20, a lui v, o sa schimbam si o sa punem 30
public class WriteHeapStatement implements IStatement{
    private String variableName;
    private IExpression expression;

    public WriteHeapStatement(String variableName, IExpression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIDictionary<String, IValue> symbolTable = state.getSymbolTable();
        MyIHeap heap = state.getHeap();
        //what was already done: Ref int v; new(v, 20); symTable: v->(1,int), Heap: 1->20
        //what we do: wH(v, 30); symTable: v->(1,int), Heap: 1->30
        //check if the variableName v is in the symTable
        if (symbolTable.lookUp(variableName) != null){
            IValue valueOfVariableName = symbolTable.lookUp(variableName);
            if (valueOfVariableName.getType() instanceof ReferenceType) {
                //check if the value in the symTable associated to the variableName v is a ReferenceType value
                //(cause this means that the value of v is allocated on the heap and we can change that value in the heap
                //if it has Ref Type it means it is  a Ref Value, so we extract the address that it contains and see if that
                //address is already allocated in the heap, to see if we can put something else there or not cu wH
                ReferenceValue referenceValue = (ReferenceValue) valueOfVariableName;
                int address = referenceValue.getAddress();
                if (heap.containsKey(address)) {
                    //we evaluate the type of the expression (30), which has to have the same type as
                    //the type of the location from the referenceValue(address, locationType)-locationType e int la noi
                    //locationType=inner, si inner e tipu cu care a fost declarat v initial. noi nu ii putem
                    //da lui v un alt tip de  valoare pe heap decat tipul de valoare cu care a fost v alocat initial
                    //si tipu de val cu care a fost v alocat initial e inner, care e egal cu locationType din ReferenceValue
                    //a lui v din symbolTable
                    IValue expressionValue = expression.evaluation(symbolTable, heap);
                    if (expressionValue.getType().equals(referenceValue.getLocationType())) {
                        heap.changeValueFromAnAddress(address, expressionValue);
                        return null;
                    }
                    else
                        throw new MyException("Expression value type and reference location type do not match");
                }
                else
                    throw new MyException("Address " + address + " is not allocated in the heap");
            } else
                throw new MyException("Variable " + variableName + " does not have a reference type");
        }
        else
            throw new MyException("Variable " + variableName + " is not defined in the symbol table");

    }

    @Override
    public IStatement deepcopy() {
        return new WriteHeapStatement(variableName, expression.deepcopy());
    }

    @Override
    public String toString() {
        return "wH(" + variableName + ", " + expression + ")";
    }

    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String,IType> typeEnvironment) throws MyException
    {
        IType typeOfVariable = typeEnvironment.lookUp(variableName);
        if (!(typeOfVariable instanceof ReferenceType))
            throw new MyException("WriteHeapStatement typecheck exception: The variable you are trying to write to is not of Reference type");
        IType typeOfExpression = expression.typecheck(typeEnvironment);
        if(!typeOfExpression.equals(((ReferenceType) typeOfVariable).getInner()))
            throw new MyException("WriteHeapStatement typecheck exception: The new expression does not have the same type as the type" +
                    "of the location where you are trying to write on the heap");
        return typeEnvironment;

    }
    //mai pe scurt puteam scrie asa:
    /* IType variableType = typeEnvironment.get(varName);
        IType expressionType = expression.typeCheck(typeEnvironment);
        // Variable type should be a reference type referencing a type that the expression should evaluate to
        if (!variableType.equals(new ReferenceType(expressionType)))
            throw new EvaluationException("Heap write: left side " + varName + " and right side " + expression + " have different types");
        return typeEnvironment;
    */
    //facem direct  IType typeOfVariable = typeEnvironment.lookUp(variableName) pt ca daca variabila nu ar fi deja declarata,
    //oricum nu o va gasi in tabel, pt ca daca e declarata, e si adaugata in tabel-asta se intampla in variableDeclarationStatement
    //daca nu o gaseste, ma gandesc ca returneaza null, si atunci tot va intra in exceptie
}
