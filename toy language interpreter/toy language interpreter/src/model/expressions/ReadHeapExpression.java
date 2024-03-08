package model.expressions;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.MyException;
import model.types.IType;
import model.types.ReferenceType;
import model.values.IValue;
import model.values.ReferenceValue;

//import java.sql.Ref;
import java.util.Set;

//TODO:Heap Reading: Define and integrate the following expression
//rH(expression)
//• the expression must be evaluated to a RefValue. If not, the execution is stopped with an appropriate error message.
//• Take the address component of the RefValue computed before and use it to access Heap table and return the value associated to that address. If the address is not a key in Heap table, the execution is stopped with an appropriate error message.
//• In order to implement the evaluation of the new expression, you have to change the signature of the eval method of the expressions classes as follows
//Value eval(MyIDictionary<String,Value> tbl, MyIHeap<Integer,Value> hp)
//Example: Ref int v;new(v,20);Ref Ref int a; new(a,v);print(rH(v));print(rH(rH(a))+5)
//At the end of execution: Heap={1->20, 2->(1,int)}, SymTable={v->(1,int), a->(2,Ref int)} and Out={20, 25}

//Example:
//Ref int v;
//new(v, 20)
//print(rH(v))-20
//rH(v) returneaza 20.
//rH e ReadHeapExpression
//rH(1) nu exista pt ca rH asteapta un RefValue(adica un IExpression care e VariableExpression, care are un id si o valoare IValue
//care e RefValue
//iar v e RefValue, ca am fc new(v, 20), care pune in SymTable
//v->(address, locationType)-care  e RefValue
public class ReadHeapExpression implements IExpression{

    IExpression expression;
    public ReadHeapExpression(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public IValue evaluation(MyIDictionary<String, IValue> symbolTable, MyIHeap heap) throws MyException
    {
        IValue valueOfExpression = expression.evaluation(symbolTable, heap);
        if(valueOfExpression instanceof ReferenceValue)
        {
            int addressOfReferenceValue = ((ReferenceValue) valueOfExpression).getAddress();

            if(heap.containsKey(addressOfReferenceValue))
            {
                return heap.lookUp(addressOfReferenceValue);
                //return the value associated to that address (for our example, 20)
                //valoarea poate sa fie int/bool/string/sau alt ref value, asta poti pune pe heap
                //pe heap am 1->20, 2->(1,int)
            }
            else
                throw new MyException("The address of the Reference Value is not a key in the Heap Table!");

        }
        else
            throw new MyException("The value of the expression is not a Reference Value!");


    }
    @Override
    public  IExpression deepcopy()
    {
        return new ReadHeapExpression(expression.deepcopy());
    }

    @Override
    public String toString()
    {
        return "rH(" + expression.toString() + ")";
    }

    public IType typecheck(MyIDictionary<String,IType> typeEnvironment) throws MyException
    {
        IType typeOfExpression = expression.typecheck(typeEnvironment);
        if (typeOfExpression instanceof ReferenceType) {
            ReferenceType referenceType =(ReferenceType) typeOfExpression;
            return referenceType.getInner();
        } else
            throw new MyException("the rH argument is not a Ref Type");
    }

    //de ce vrea sa returnam tipul valorii de pe heap? adica ref(a), de ce vrea sa returnam tipul lui a?

}
