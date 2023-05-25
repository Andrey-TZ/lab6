package classes.utils;

import classes.commands.AbstractCommand;

import java.util.HashMap;
import java.util.Scanner;

public class CommandExecutor implements Runnable {
    private final CollectionManager collectionManager;

    /**
     * Constructor for command executor
     *
     * @param collectionManager CollectionManager class object
     */

    public CommandExecutor(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public void run() {
        Scanner commandReader = new Scanner(System.in);
        while (true) {
            System.out.print("Введите команду: ");
            if (!commandReader.hasNextLine()) System.exit(0);
            String[] arguments = commandReader.nextLine().trim().toLowerCase().split("\\s+");
            if (arguments[0].trim().equals("save")) collectionManager.save();
            else if (arguments[0].trim().equals("exit")) {
                collectionManager.save();
                System.exit(0);
            }
        }

    }

}
