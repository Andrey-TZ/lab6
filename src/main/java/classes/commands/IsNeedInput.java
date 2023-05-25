package classes.commands;

import exceptions.NotEnoughArgumentsException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongArgumentException;

import java.io.BufferedReader;
import java.io.IOException;

public interface IsNeedInput {
    public Object[] validate(String[] args) throws NotEnoughArgumentsException, WrongArgumentException;

    public Object[] validateFromFile(BufferedReader reader, String[] args) throws NotEnoughLinesException, IOException, NotEnoughArgumentsException, WrongArgumentException;
}
