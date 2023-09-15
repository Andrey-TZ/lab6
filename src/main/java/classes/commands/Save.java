package classes.commands;

import classes.dataBase.UserData;
import classes.shells.ArgsShell;
import classes.shells.Response;
import exceptions.NotEnoughArgumentsException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongArgumentException;
import classes.utils.CollectionManager;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Command to save collection to the same file
 */
public class Save extends AbstractCommand{

    public Save(){
        this.description = "сохранить коллекцию в файл";
        this.name = "save";
    }

    @Override
    public Response execute(CollectionManager collectionManager, ArgsShell args, UserData user)  {
        collectionManager.addToHistory(this, user);
        return new Response("Коллекция успешно сохранена");
    }
}
