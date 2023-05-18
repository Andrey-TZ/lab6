package classes.commands;

import classes.shells.ArgsShell;
import classes.shells.Response;
import exceptions.NotEnoughArgumentsException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongArgumentException;
import classes.utils.CollectionManager;

import java.io.BufferedReader;

/**
 * Command to delete element from collection by given key
 */
public class RemoveKey extends AbstractCommand implements IsNeedInput {
    public RemoveKey() {
        this.name = "remove_key key";
        this.description = "удалить элемент коллекции по ключу";
    }



    @Override
    public Response execute(CollectionManager collectionManager, ArgsShell args) {
        int key = (int) args.getArguments()[0];
        collectionManager.addToHistory(this);
        if (collectionManager.isKeyExist(key)){
            collectionManager.removeByKey(key);
            return new Response("Элемент с ключом \"" + key + "\" успешно удалён");
        }
        return new Response("Элемент с ключом \"" + key + "\" не существует");
    }

    @Override
    public void executeFromFile(BufferedReader reader, String[] args, CollectionManager collectionManager) throws NotEnoughLinesException, WrongArgumentException, NotEnoughArgumentsException {
    }

    @Override
    public Object[] validate(String[] args) throws NotEnoughArgumentsException, WrongArgumentException {
        if (args.length < 2) throw new NotEnoughArgumentsException("команда требует аргумент \"key\"");
        int key;
        try {
            key = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new WrongArgumentException("аргумент должен быть числом!");
        }
        return new Object[]{key};
    }

    @Override
    public boolean isNeedInput() {
        return true;
    }
}
