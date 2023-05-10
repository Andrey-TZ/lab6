package classes.commands;

import exceptions.NotEnoughArgumentsException;
import exceptions.WrongArgumentException;

public interface IsNeedInput {
    public Object[] validate(String[] args) throws NotEnoughArgumentsException, WrongArgumentException;
}
