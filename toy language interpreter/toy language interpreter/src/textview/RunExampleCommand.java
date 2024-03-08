package textview;

import model.MyException;
import controller.Controller;
import textview.Command;

public class RunExampleCommand extends Command {
    private Controller controller;
    public RunExampleCommand(String key, String description,Controller ctr){
        super(key, description);
        this.controller =ctr;
    }
    @Override
    public void execute() {
        try{
            controller.allStep(); }
        catch (MyException exception) {
             System.out.println(exception.getMessage());
        } //here you must treat the exceptions that can not be solved in the controller
    }
}