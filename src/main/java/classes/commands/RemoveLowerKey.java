package classes.commands;

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
    public void execute(String[] args, CollectionManager collectionManager) throws NotEnoughArgumentsException, WrongArgumentException {
        if(args.length < 2) throw new NotEnoughArgumentsException("команда требует аргумент \"key\"");
        int key;
        try{
            key = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e){
            throw new WrongArgumentException("аргумент должен быть числом!");
        }
        collectionManager.removeLowerKey(key);
        System.out.print("Элемент с ключом \"");
        System.out.print(key);
        System.out.println("\" успешно удалён");
        collectionManager.addToHistory(this);
    }

    @Override
    public void executeFromFile(BufferedReader reader, String[] args, CollectionManager collectionManager) throws NotEnoughLinesException, WrongArgumentException, NotEnoughArgumentsException {
        execute(args, collectionManager);
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
