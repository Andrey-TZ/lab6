package classes.commands;

import classes.model.StudyGroup;
import classes.shells.ArgsShell;
import classes.shells.Response;
import exceptions.NotEnoughArgumentsException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongArgumentException;
import classes.utils.CollectionManager;

import java.io.BufferedReader;
import java.util.Set;

/**
 * Command to display all elements whose "name" value starts from the given one
 */
public class FilterStartsWithName extends AbstractCommand implements IsNeedInput {
    public FilterStartsWithName() {
        this.name = "filter_starts_with_name name";
        this.description = "вывести элементы, значения \"name\" которых начинается с заданной подстроки";
    }

    @Override
    public Response execute(CollectionManager collectionManager, ArgsShell args) {
        String name = (String) args.getArguments()[0];
        Set<StudyGroup> groups = collectionManager.filterStartsWithName(name);
        Response response = new Response("Группы, начинающиеся с \"" + name + "\":");
        for (StudyGroup group : groups) {
            response.setData(group.toString());
        }
        collectionManager.addToHistory(this);
        return response;
    }

    @Override
    public Object[] validate(String[] args) throws NotEnoughArgumentsException {
        if (args.length < 2) throw new NotEnoughArgumentsException("\"команда требует аргумент \\\"name\\\"\"");
        String name = args[1];
        return new Object[]{name};
    }

    @Override
    public Object[] validateFromFile(BufferedReader reader, String[] args) throws NotEnoughArgumentsException {
        return validate(args);
    }

    @Override
    public boolean isNeedInput() {
        return true;
    }
}
