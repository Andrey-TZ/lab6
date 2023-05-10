package classes.commands;

import exceptions.NotEnoughArgumentsException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongArgumentException;
import classes.utils.CollectionManager;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Command to display all elements from the collection
 */
public class Show extends AbstractCommand{
    public Show(){
        this.name = "show";
        this.description = "вывести все элементы коллекции";
    }
    /**
     * @param args
     * @param collectionManager
     * @throws NotEnoughArgumentsException
     * @throws WrongArgumentException
     */
    @Override
    public void execute(String[] args, CollectionManager collectionManager) throws NotEnoughArgumentsException, WrongArgumentException {
        collectionManager.show();
        collectionManager.addToHistory(this);
    }

    @Override
    public void executeFromFile(BufferedReader reader, String[] args, CollectionManager collectionManager) throws NotEnoughLinesException, WrongArgumentException, NotEnoughArgumentsException {
        execute(args, collectionManager);
    }
}
