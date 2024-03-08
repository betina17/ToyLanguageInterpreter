package model.statements;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.ADTs.MyIStack;
import model.MyException;
import model.ProgramState;
import model.expressions.IExpression;
import model.types.ReferenceType;
import model.values.IValue;
import model.values.ReferenceValue;
import model.types.IType;

//aici alocam o variabila pe heap. new(var_name, expression). expresia trebuie sa aiba tip RefType
//si sa aiba aceeasi valoare ca si valoarea de la locatia
public class HeapAllocationStatement implements IStatement{
    String variableName;
    IExpression expression;

    public HeapAllocationStatement(String variableName, IExpression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    //Ref int v; v->Ref(inner)
    //noi avem deja declararea variabilei v asa prin new VariableDeclaration("v", new ReferenceType(new IntType()))
    //new(v, expression) -> v->(address, inner)
    //acum vrem sa ii asociem o valoare acestei variabile v, iar valoarea vrem sa o punem pe heap.
    //adica cream un NewReferenceStatement, prin care se face HeapAllocation.
    //dar inainte de asta, trebuie sa verificam daca variabila v este declarata ca fiind de tip referinta, ca sa putem sa ii
    //alocam valoarea pe heap, si sa verificam daca inner == expression.getType(), adica daca tipul valorii din expresie
    //este acelasi cu ce tip am declarat ca vom avea la locatia de pe heap de cand am declarat Ref int v, care e inner (in cazul
    //nostru inner e un int)
    //daca toate astea sunt in regula, alocam valoarea noii expresii pe care vrem sa o asociem variabilei v pe heap, si
    //updatam in symbol table faptul ca v nu mai e doar o variabila declarata, ci are asociata ei o expresie (prin new(v, expr))
    //acea expresie fiind NewReferenceStatement, care face HeapAllocation, punem in dreptul lui v in SymTable
    //v->(address, inner), unde inner e tipul valorii din expresie, si address e adresa la care am pus valoarea din expresie
    //ca sa stim ca v e alocata pe heap, pt ca are adresa si tipul valorii din expresia alocata lui v, valoare care e pusa pe heap
    //deci valoarea lui v va fi ReferenceValue(address, locationType)
    @Override
    public ProgramState execute(ProgramState state) throws MyException
    {
        MyIDictionary<String, IValue> symbolTable = state.getSymbolTable();
        MyIHeap heap = state.getHeap();
        if(symbolTable.lookUp(variableName)!=null) {
            if (symbolTable.lookUp(variableName).getType() instanceof ReferenceType) {
                MyIStack<IStatement> stack = state.getExecutionStack();         //in symTable acum avem: v->Ref(inner)
                IValue valueOfExprInSymtable = expression.evaluation(symbolTable, heap);  //get the value of the expression in the symbol table
                                                                                    //asta dupa declarare va fi default_value, pt ref e 0
                IType exprValueTypeInSymTable = valueOfExprInSymtable.getType();  //get INNER-the type of the expression in the symbol table
                IType referenceValueLocationType = ((ReferenceValue) symbolTable.lookUp(variableName)).getLocationType(); //get the type of the value of the heap location (Ref int v)
                                                                                                    //din new(var, expression)
                if (!exprValueTypeInSymTable.equals(referenceValueLocationType)) {  //inner == expression.getType()
                    throw new MyException("The expression type and the reference location type are not the same!");

                }
            }
            else
                throw new MyException("This variable is not declared as a reference type variable!");
        }
        else
            throw new MyException("The variable is not declared in the symbol table!");

        int idx = heap.update(expression.evaluation(symbolTable, heap));
        //heap.update returneaza adresa de pe heap pe care punem
        //update symtable dupa atribuirea unei valori pe heap lui v

        //so we declared the new variable as a Reference type of variable: Ref int v
        //so in the symbol table that will look like this: v -> Ref(inner), care inner e int/bool/string/ref
        //acum, dupa ce am dat o valoare acestei variabile v, si i-am pus valoarea pe heap, prin HeapAllocation, care e fix
        //ceeaa ce facem in acest NewReferenceStatement, trebuie sa facem update la valoarea din symbol table a variabilei v
        //adica in SymTable vom avea: v->(address, locationType), unde locationType e egal cu inner(pt ca am pus conditie ca
        // , adica int/bool/string/ref
        //adica ii asociem lui v un ReferenceValue(address, locationType)

        symbolTable.update(variableName, new ReferenceValue(idx, expression.evaluation(symbolTable,heap).getType()));
        return null;

    }

    public String toString()
    {
        return "new(" + variableName + "," + expression.toString() + ")";
    }
    @Override
    public IStatement deepcopy()
    {
        return new HeapAllocationStatement(variableName, expression.deepcopy());
    }

    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String,IType> typeEnvironment) throws MyException
    {
        IType typeOfVariable = typeEnvironment.lookUp(variableName); //va returna ReferenceType(inner)
        IType typeOfExpression = expression.typecheck(typeEnvironment);
        if(! typeOfVariable.equals(new ReferenceType(typeOfExpression))) //daca variabila e reference type si inner-ul, adica
            //tipul locatiei (Ref int a-acest int) e acelasi cu tipul expresiei pe care vrei sa o pui la locatia respectiva, e bine
            throw new MyException("HeapAllocationStatement execution exception: the right hand side and the left hand side are not both reference types!");
        return typeEnvironment;
    }

}
