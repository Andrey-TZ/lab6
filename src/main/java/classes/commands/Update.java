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
 * Command to update the element by its key
 */
public class Update extends AbstractCommand implements IsNeedInput {
    public Update() {
        this.description = "обновить значение элемента, id которого равен заданному";
        this.name = "update id";
    }

    @Override
    public Response execute(CollectionManager collectionManager, ArgsShell args, UserData user) {
        int id = (int) args.getArguments()[0];
        StudyGroup group = (StudyGroup) args.getArguments()[1];
        collectionManager.addToHistory(this, user);
        return new Response(collectionManager.update(id, group, user));
    }

    @Override
    public boolean isNeedInput() {
        return true;
    }

    @Override
    public Object[] validate(String[] args) throws NotEnoughArgumentsException, WrongArgumentException {
        if (args.length < 2) throw new NotEnoughArgumentsException("команда требует аргумент \"id\"");
        int id;
        try {
            id = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new WrongArgumentException("аргумент должен быть числом! ");
        }
        StudyGroup group = new StudyGroup();
        CLIManager.requestStudyGroup(group);
        return new Object[]{id, group};
    }

    @Override
    public Object[] validateFromFile(BufferedReader reader, String[] args) throws NotEnoughLinesException, IOException, NotEnoughArgumentsException, WrongArgumentException {
        if (args.length < 2) throw new NotEnoughArgumentsException("команда требует аргумент \"id\"");
        int id;
        try {
            id = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new WrongArgumentException("аргумент должен быть числом! ");
        }
        StudyGroup group = new StudyGroup();
        ScriptManager manager = new ScriptManager(reader);
        manager.requestStudyGroup(group);
        return new Object[]{id, group};
    }
}
