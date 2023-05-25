package classes.commands;

import classes.model.Person;
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
import java.util.Set;

/**
 * Command to display all elements whose "groupAdmin" value is less than the given one
 */
public class FilterLessThanGroupAdmin extends AbstractCommand implements IsNeedInput {
    public FilterLessThanGroupAdmin() {
        this.name = "filter_less_than_group_admin {groupAdmin}";
        this.description = "вывести все элементы, значение \"groupAdmin\" которых меньше заданного";
    }

    @Override
    public Response execute(CollectionManager collectionManager, ArgsShell args) {
        collectionManager.addToHistory(this);
        Person groupAdmin = (Person) args.getArguments()[0];
        if (collectionManager.isEmpty()) {
            return new Response("Нет элементов для сравнения");
        }
        Set<StudyGroup> groups = collectionManager.filterLessThanGroupAdmin(groupAdmin);
        if (groups == null) {
            return new Response("Элементы с заданным фильтром не найдены");
        }
        Response response = new Response("Найдены группы:");
        for (StudyGroup group : groups) {
            response.setData(group.toString());
        }
        return response;
    }

    public Object[] validate(String[] args) throws NotEnoughArgumentsException, WrongArgumentException {
        Person groupAdmin = CLIManager.requestAdminGroup();
        return new Object[]{groupAdmin};
    }

    @Override
    public Object[] validateFromFile(BufferedReader reader, String[] args) throws NotEnoughLinesException, IOException {
        ScriptManager manager = new ScriptManager(reader);
        Person groupAdmin = manager.requestAdminGroup();
        return new Object[]{groupAdmin};
    }

    @Override
    public boolean isNeedInput() {
        return true;
    }
}
