package classes.commands;

import classes.model.StudyGroup;
import classes.shells.ArgsShell;
import classes.shells.Response;
import exceptions.NotEnoughArgumentsException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongArgumentException;
import classes.utils.CollectionManager;

import java.io.BufferedReader;
import java.util.Collection;

/**
 * Command to display all elements from the collection
 */
public class Show extends AbstractCommand {
    public Show() {
        this.name = "show";
        this.description = "вывести все элементы коллекции";
    }

    @Override
    public Response execute(CollectionManager collectionManager, ArgsShell args) {
        collectionManager.addToHistory(this);
        Collection<StudyGroup> elements= collectionManager.show();
        if (elements.size() == 0) return new Response("В коллекции ещё нет ни одного элемента");
        Response response = new Response();
        for(StudyGroup group: elements){
            response.setData(group.toString());
        }
        return response;
    }

    @Override
    public void executeFromFile(BufferedReader reader, String[] args, CollectionManager collectionManager) throws NotEnoughLinesException, WrongArgumentException, NotEnoughArgumentsException {
    }
}
