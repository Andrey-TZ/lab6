package classes.commands;

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
    public void execute(String[] args, CollectionManager collectionManager) {
        System.out.println("Программа будет закрыта");
        System.exit(0);
    }

    @Override
    public void executeFromFile(BufferedReader reader, String[] args, CollectionManager collectionManager) throws NotEnoughLinesException, WrongArgumentException, NotEnoughArgumentsException {
        execute(args, collectionManager);
    }

}
