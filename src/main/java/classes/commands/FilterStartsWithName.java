package classes.commands;

import classes.model.StudyGroup;
import exceptions.NotEnoughArgumentsException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongArgumentException;
import classes.utils.CollectionManager;

import java.io.BufferedReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Command to display all elements whose "name" value starts from the given one
 */
public class FilterStartsWithName extends AbstractCommand implements IsNeedInput{
    public FilterStartsWithName(){
        this.name = "filter_starts_with_name name";
        this.description = "вывести элементы, значения \"name\" которых начинается с заданной подстроки";
    }
    @Override
    public void execute(String[] args, CollectionManager collectionManager) throws NotEnoughArgumentsException, WrongArgumentException {
        if(args.length < 2) throw new NotEnoughArgumentsException("команда требует аргумент \"name\"");
        String name = args[1];
        Set<StudyGroup> groups = collectionManager.filterStartsWithName(name);
        Set set = new HashSet();
        HashSet h = new HashSet();

        for(StudyGroup group : groups){
            System.out.println(group);
        }
        collectionManager.addToHistory(this);
    }


    @Override
    public void executeFromFile(BufferedReader reader, String[] args, CollectionManager collectionManager) throws NotEnoughLinesException, WrongArgumentException, NotEnoughArgumentsException {
        execute(args, collectionManager);
    }

    @Override
    public Object[] validate(String[] args) throws NotEnoughArgumentsException {
        if (args.length < 2) throw new NotEnoughArgumentsException("\"команда требует аргумент \\\"name\\\"\"");
        String name = args[1];
        return new Object[]{name};
    }
    @Override
    public boolean isNeedInput(){
        return true;
    }
}
