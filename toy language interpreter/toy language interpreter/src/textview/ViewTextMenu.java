package textview;

import textview.Command;

import java.util.*;

public class ViewTextMenu {



    private Map<String, Command> commands;

    public ViewTextMenu() {

        commands = new HashMap<>();

    }

    public void addCommand(Command c) {
        commands.put(c.getKey(), c);
    }

    private void printMenu() {
        for (Command com : commands.values()) {
            String line = String.format("%4s : %s", com.getKey(), com.getDescription());
            System.out.println(line);
        }
    }

    public void show() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            System.out.printf("Enter the program you want to be executed, the first one being indexed with 1. To exit, press 0. Introduce your option> ");
            String key = scanner.nextLine();
            Command com = commands.get(key);
            if (com == null) {
                System.out.println("Invalid Option");
                continue;
            }
            com.execute();
        }
    }
}