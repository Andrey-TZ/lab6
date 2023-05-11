package classes.commands;

import classes.shells.ArgsShell;
import classes.shells.Response;
import exceptions.NotEnoughArgumentsException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongArgumentException;
import classes.utils.CollectionManager;

import java.io.BufferedReader;


/**
 * Command which removes all elements of the collection
 */
public class Clear extends AbstractCommand {
    public Clear() {
        this.name = "clear";
        this.description = "очистить коллекцию";
    }

    @Override
    public Response execute(CollectionManager collectionManager, ArgsShell args) throws NotEnoughArgumentsException, WrongArgumentException {
        collectionManager.clear();
        collectionManager.addToHistory(this);
        Response response = new Response();
        response.setData("Коллекция успешно ощищена");
        return response;
    }

    @Override
    public void executeFromFile(BufferedReader reader, String[] args, CollectionManager collectionManager) throws NotEnoughLinesException, WrongArgumentException, NotEnoughArgumentsException {
    }
}
