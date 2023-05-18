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
        this.description = "завершить программу (без сохранения в файл)";
    }

    @Override
    public Response execute(CollectionManager collectionManager, ArgsShell args) {
        System.out.println("Программа будет закрыта");
        collectionManager.addToHistory(this);
        Runtime.getRuntime().exit(0);
        return new Response("Программа будет закрыта");

    }

    @Override
    public void executeFromFile(BufferedReader reader, String[] args, CollectionManager collectionManager) throws NotEnoughLinesException, WrongArgumentException, NotEnoughArgumentsException {
    }

}
