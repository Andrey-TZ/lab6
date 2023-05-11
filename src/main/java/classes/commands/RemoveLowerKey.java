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
 * Command to remove all elements from the collection, which has keys less than a given one
 */
public class RemoveLowerKey extends AbstractCommand implements IsNeedInput{
    public RemoveLowerKey(){
        this.name = "remove_lower_key key";
        this.description = "удалить элементы, ключ которых меньше, чем заданный";
    }



    @Override
    public Response execute(CollectionManager collectionManager, ArgsShell args) throws NotEnoughArgumentsException, WrongArgumentException {
        int key = (int) args.getArguments()[0];
        int deleted = collectionManager.removeLowerKey(key);
        collectionManager.addToHistory(this);
        if (deleted == 0)return new Response("Ни один элемент не удалён");
        return new Response("Удалено элементов: " + deleted);

    }

    @Override
    public void executeFromFile(BufferedReader reader, String[] args, CollectionManager collectionManager) throws NotEnoughLinesException, WrongArgumentException, NotEnoughArgumentsException {
    }
    @Override
    public boolean isNeedInput() {
        return true;
    }


    @Override
    public Object[] validate(String[] args) throws NotEnoughArgumentsException, WrongArgumentException {
        if(args.length < 2) throw new NotEnoughArgumentsException("команда требует аргумент \"key\"");
        int key;
        try{
            key = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e){
            throw new WrongArgumentException("аргумент должен быть числом!");
        }
        return new Object[]{key};
    }
}
