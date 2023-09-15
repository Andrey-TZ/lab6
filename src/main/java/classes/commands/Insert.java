package classes.commands;

import classes.dataBase.UserData;
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
    public Response execute(CollectionManager collectionManager, ArgsShell args, UserData user) {
        int key = (int) args.getArguments()[0];
        StudyGroup group = (StudyGroup) args.getArguments()[1];
        group.checkId();
        Response response = new Response();
        try {
            response.setData(collectionManager.insert(key, group, user));
        } catch (WrongArgumentException e) {
            response.setData(e.getMessage());
        }
        collectionManager.addToHistory(this, user);
        return response;

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
    public Object[] validateFromFile(BufferedReader reader, String[] args) throws NotEnoughLinesException, IOException, NotEnoughArgumentsException, WrongArgumentException {
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
        return new Object[]{key, group};
    }

    @Override
    public boolean isNeedInput() {
        return true;
    }
}
