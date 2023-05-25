package classes.commands;

import classes.shells.ArgsShell;
import classes.shells.Response;
import exceptions.NotEnoughArgumentsException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongArgumentException;
import classes.utils.CollectionManager;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Command to exit the program
 */
public class Exit extends AbstractCommand{
    public Exit(){
        this.name = "exit";
        this.description = "завершить программу (с сохранением в файл)";
    }

    @Override
    public Response execute(CollectionManager collectionManager, ArgsShell args) {
        System.out.println("Программа будет закрыта");
        collectionManager.addToHistory(this);
        Response response = new Response(collectionManager.save());
        System.out.println("Коллекция сохранена");
        response.setData("Программа будет закрыта");
        response.setLastResponse();
        return response;
    }
}
