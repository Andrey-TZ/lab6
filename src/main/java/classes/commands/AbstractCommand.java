package classes.commands;

import classes.shells.ArgsShell;
import classes.shells.Response;
import exceptions.NotEnoughArgumentsException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongArgumentException;
import classes.utils.CollectionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;

/**
 * Abstract class for all commands.
 * Implementations - all command classes
 * @author Andrey Vorotnikov
 */

public abstract class AbstractCommand implements Serializable {
    protected String name;
    protected String description;

    /**
     *  Method to execute command
     * @param collectionManager object working with collection
     * @throws NotEnoughArgumentsException  when argument's count does not match requirements.
     * @throws WrongArgumentException when argument doesn't match requirements.
     */
    public abstract Response execute(CollectionManager collectionManager, ArgsShell args);


    public boolean isNeedInput(){
        return false;
    }

    /**
     *
     * @return name of the command
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return description of the command
     */
    public String getDescription() {
        return description;
    }
}

