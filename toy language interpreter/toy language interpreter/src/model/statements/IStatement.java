package model.statements;

import model.MyException;
import model.ProgramState;
import model.types.IType;
import model.ADTs.MyIDictionary;


public interface IStatement {
    ProgramState execute(ProgramState state) throws MyException;
    IStatement deepcopy();

    MyIDictionary<String,IType> typeCheck(MyIDictionary<String,IType> typeEnvironment) throws MyException;
    //la statementuri trb sa faci typecheck la statementurile si expresiile care le compun, si la variabilele care le compun
    //sa le cauti in dictionarul typeEnvironment  si sa le iei tipul
   //returnezi dictionarul cu?-cu noile variabile adaugate si tipurile lor, dar de ce se ajunge mereu la variabiledeclaration?-vezi comp stmt
    //populezi dictionarul in VariableDeclarationStatement, acolo adaugi varibilele
}
