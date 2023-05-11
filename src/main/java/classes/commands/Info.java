package classes.commands;

import classes.shells.ArgsShell;
import classes.shells.Response;
import exceptions.NotEnoughArgumentsException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongArgumentException;
import classes.utils.CollectionManager;

import java.io.BufferedReader;

/**
 * Command to display information about collection
 */
public class Info extends AbstractCommand {
    public Info() {
        this.name = "info";
        this.description = "вывести информацию о коллекции";
    }

    @Override
    public Response execute(CollectionManager collectionManager, ArgsShell args) throws NotEnoughArgumentsException, WrongArgumentException {
        collectionManager.addToHistory(this);
        return new Response(collectionManager.info());
    }

    @Override
    public void executeFromFile(BufferedReader reader, String[] args, CollectionManager collectionManager) throws NotEnoughLinesException, WrongArgumentException, NotEnoughArgumentsException {
    }
}
