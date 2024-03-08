package view.textview;

/*public class View {
    Controller controller;

    public View(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    public void start()
    {
        System.out.println("Enter the program you want to be executed, the first one being indexed with 0. " +
                "There are currently " + controller.getRepository().getListOfProgramStates().size() +
                " hardcoded programs");
        System.out.println("Enter -1 to quit");
        System.out.println(">");
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        controller.getRepository().setIndex(input);
        System.out.println("Enter the path to the file where you want the program execution to be written");
        System.out.println(">");
        String path = scanner.next();
        controller.getRepository().setLogPathFile(path);
       ProgramState chosenProgram = controller.getRepository().getCurrentProgramState();
       try {
           controller.executeAllStatementsOfAProgram();
       }
       catch(MyException exception)
       {
           System.out.println(exception);
       }
    }
}
*/