package classes.commands;

import classes.dataBase.UserData;
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
    public Response execute(CollectionManager collectionManager, ArgsShell args, UserData user) {
        String result = collectionManager.clear(user);
        collectionManager.addToHistory(this, user);
        Response response = new Response();
        response.setData(result);
        return response;
    }
}
