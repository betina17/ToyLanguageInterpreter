package model.expressions;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.MyException;
import model.types.IType;
import model.values.IValue;

public class VariableExpression implements IExpression{
    String id; //the name of the variable

    public VariableExpression(String id) {
        this.id = id;
    }
    @Override
    public IValue evaluation(MyIDictionary<String, IValue> symbolTable, MyIHeap heap) throws MyException
    {
        if(symbolTable.lookUp(id)!=null)  //nu poti folosi VariableExpression("v") daca v nu a fost deja declarata prin
            return symbolTable.lookUp(id); //new VariableDeclarationStatement("v", new Type()), iar atunci cand o declari
        else                                //asa o pui in symbtable: symbolTable.update(id, type.defaultValue());
                                        //adica ii dai default value. ei,  if(symbolTable.lookUp(id)!=null) va fi true doar daca
                                        //valoarea returnata de lookup nu e nula, adica daca variabila a fost declarata
                                        //si a primit default value
            throw new MyException("This variable is not defined");
    }
    public String toString()
    {
        return id; //returneaza numele variabilei
    }
    @Override
    public IExpression deepcopy()
    {
        return new VariableExpression(id); //fara .deepcopy() ca e string, care e primitiv
                                            //daca nu ar fi primitiv, ar trb sa fac expr.deepcopy(), pt ca expr ar fi tot un obiect
        //si daca nu ii fac deepcopy la param de tip obiect, cand creez obiect nou de tip VariableExpression(expr),
        //in loc de VariableExpression(expr.deepcopy()), practic noul obiect VariableExpression va arata spre acelasi
        //obiect expr, deci n-am rezolvat nimic cu implementarea deepcopy in VariableExpression, ci doar ca am fc 2
        //obiecte VariableExpression sa arate spre acelasi obeict expr. asta nu se aplica aici la VariableExpression
        //pt ca asta are field de tip priimitv, dar se aplica la clasele care au field-uri de tip obiect.
    }
    public IType typecheck(MyIDictionary<String,IType> typeEnvironment) throws MyException
    {
        return typeEnvironment.lookUp(id);
    }
    //returnam asta pt ca noi in acest dictionar avem <Id, Type> la fiecare chestie din symtable, de aia cautam in acest dictionar
    //dupa id cu lookup, iar lookup returneaza valoarea de la cheia respectiva, in acest dictionar valoarea fiind tipul variabilei

}
