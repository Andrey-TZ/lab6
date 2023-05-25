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
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Command to remove all elements from the collection witch less than a given one
 */



public class RemoveLower extends AbstractCommand implements IsNeedInput{
    public RemoveLower() {
        this.name = "remove_lower {element}";
        this.description = "удаляет элементы коллекции меньшие чем введённый";
    }

    @Override
    public Response execute(CollectionManager collectionManager, ArgsShell args) {
        StudyGroup group = (StudyGroup) args.getArguments()[0];
        int deleted = collectionManager.removeLower(group);
        collectionManager.addToHistory(this);
        if(deleted == 0) return new Response("Элементов для удаления нет");
        return new Response("Удалено элементов: " + deleted);
    }

    @Override
    public void executeFromFile(BufferedReader reader, String[] args, CollectionManager collectionManager) throws NotEnoughLinesException, WrongArgumentException, NotEnoughArgumentsException, IOException {
        if (collectionManager.isEmpty()) {
            System.out.println("Элементов для удаления нет!");
            return;
        }
        ScriptManager manager = new ScriptManager(reader);
        StudyGroup group = new StudyGroup();
        manager.requestStudyGroup(group);
        collectionManager.removeLower(group);
        collectionManager.addToHistory(this);
    }


    @Override
    public Object[] validate(String[] args) throws NotEnoughArgumentsException, WrongArgumentException {
        StudyGroup group = new StudyGroup();
        CLIManager.requestStudyGroup(group);
        return new Object[] {group};
    }

    @Override
    public Object[] validateFromFile(BufferedReader reader, String[] args) throws NotEnoughLinesException, IOException, NotEnoughArgumentsException, WrongArgumentException {
        ScriptManager manager = new ScriptManager(reader);
        StudyGroup group = new StudyGroup();
        manager.requestStudyGroup(group);
        return new Object[]{group};
    }

    @Override
    public boolean isNeedInput() {
        return true;
    }
}
