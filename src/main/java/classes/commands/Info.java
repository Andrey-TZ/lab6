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
 * Command to display information about collection
 */
public class Info extends AbstractCommand {
    public Info() {
        this.name = "info";
        this.description = "вывести информацию о коллекции";
    }

    @Override
    public Response execute(CollectionManager collectionManager, ArgsShell args, UserData user) {
        collectionManager.addToHistory(this, user);
        return new Response(collectionManager.info());
    }
}
