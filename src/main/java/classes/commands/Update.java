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
 * Command to update the element by its key
 */
public class Update extends AbstractCommand implements IsNeedInput {
    public Update() {
        this.description = "обновить значение элемента, id которого равен заданному";
        this.name = "update id";
    }

    @Override
    public Response execute(CollectionManager collectionManager, ArgsShell args) {
        int id = (int) args.getArguments()[0];
        StudyGroup group = (StudyGroup) args.getArguments()[1];
        collectionManager.addToHistory(this);
        return new Response(collectionManager.update(id, group));
    }

    @Override
    public void executeFromFile(BufferedReader reader, String[] args, CollectionManager collectionManager) throws NotEnoughLinesException, WrongArgumentException, NotEnoughArgumentsException, IOException {
        if (args.length < 2) throw new NotEnoughArgumentsException("команда требует аргумент \"id\"");
        int id;
        try {
            id = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new WrongArgumentException("аргумент должен быть числом! ");
        }
        StudyGroup group = collectionManager.getById(id);
        if (group == null) throw new WrongArgumentException("Элемент с таким id не найден. ");
        ScriptManager manager = new ScriptManager(reader);
        manager.requestStudyGroup(group);

        System.out.println("Элемент успешно обновлён");
        collectionManager.addToHistory(this);
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
        if (group == null) throw new WrongArgumentException("Элемент с таким id не найден. ");
        ScriptManager manager = new ScriptManager(reader);
        manager.requestStudyGroup(group);
        return new Object[]{id, group};
    }
}
