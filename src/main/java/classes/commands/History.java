package classes.commands;

import classes.dataBase.UserData;
import classes.shells.ArgsShell;
import classes.shells.Response;
import exceptions.NotEnoughArgumentsException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongArgumentException;
import classes.utils.CollectionManager;

import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * Command to display the last 15 used commands
 */
public class History extends AbstractCommand {
    public History() {
        this.name = "history";
        this.description = "вывести последние 15 команд без их аргументов";
    }

    @Override
    public Response execute(CollectionManager collectionManager, ArgsShell args, UserData user) {
        ArrayList<String> history = collectionManager.getHistory(user);
        Response response = new Response("Последние команды: ");
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < history.size(); i++) {
            if (i != history.size() - 1) data.append(history.get(i)).append(", ");
            else data.append(history.get(i)).append(".");
        }
        response.setData(String.valueOf(data));
        collectionManager.addToHistory(this, user);
        return response;
    }
}
