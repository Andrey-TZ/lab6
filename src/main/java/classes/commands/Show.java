package classes.commands;

import classes.shells.ArgsShell;
import classes.shells.Response;
import exceptions.NotEnoughArgumentsException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongArgumentException;
import classes.utils.CollectionManager;

import java.io.BufferedReader;

/**
 * Command to display all elements from the collection
 */
public class Show extends AbstractCommand {
    public Show() {
        this.name = "show";
        this.description = "вывести все элементы коллекции";
    }

    @Override
    public Response execute(CollectionManager collectionManager, ArgsShell args) {
        collectionManager.addToHistory(this);
        return new Response(collectionManager.show());
    }

    @Override
    public void executeFromFile(BufferedReader reader, String[] args, CollectionManager collectionManager) throws NotEnoughLinesException, WrongArgumentException, NotEnoughArgumentsException {
    }
}
