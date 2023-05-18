package classes.commands;

import classes.model.StudyGroup;
import classes.shells.ArgsShell;
import classes.shells.Response;
import classes.utils.CLIManager;
import classes.utils.ScriptManager;
import exceptions.NotEnoughArgumentsException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongArgumentException;
import classes.utils.CollectionManager;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Command to insert element to collection with given key
 */
public class Insert extends AbstractCommand implements IsNeedInput {
    public Insert() {
        this.name = "insert key";
        this.description = "добавить новый элемент с заданным ключом";
    }

    @Override
    public Response execute(CollectionManager collectionManager, ArgsShell args)  {
        int key = (int) args.getArguments()[0];
        StudyGroup group =  (StudyGroup) args.getArguments()[1];
        Response response = new Response();
        try {
            collectionManager.insert(key, group);
            response.setData("Элемент успешно добавлен");
        }
        catch (WrongArgumentException e){
            response.setData(e.getMessage());
        }
        collectionManager.addToHistory(this);
        return response;

    }

    @Override
    public void executeFromFile(BufferedReader reader, String[] args, CollectionManager collectionManager) throws NotEnoughLinesException, WrongArgumentException, NotEnoughArgumentsException, IOException {
        if (args.length < 2) throw new NotEnoughArgumentsException("команда требует аргумент \"key\"");
        int key;
        try {
            key = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new WrongArgumentException("аргумент должен быть числом! ");
        }
        ScriptManager manager = new ScriptManager(reader);
        StudyGroup group = new StudyGroup();
        manager.requestStudyGroup(group);
        collectionManager.insert(key, group);
        System.out.println("Элемент успешно добавлен");
        collectionManager.addToHistory(this);
    }

    @Override
    public Object[] validate(String[] args) throws NotEnoughArgumentsException, WrongArgumentException {
        if (args.length < 2) throw new NotEnoughArgumentsException("команда требует аргумент \"key\"");
        int key;
        try {
            key = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new WrongArgumentException("аргумент должен быть числом! ");
        }
        StudyGroup group = new StudyGroup();
        CLIManager.requestStudyGroup(group);
        return new Object[]{key, group};
    }
    @Override
    public boolean isNeedInput(){return true;}
}
