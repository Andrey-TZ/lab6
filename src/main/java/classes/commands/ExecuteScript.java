package classes.commands;

import classes.dataBase.UserData;
import classes.shells.ArgsShell;
import classes.shells.Request;
import classes.shells.Response;
import classes.utils.CollectionManager;
import classes.utils.CommandManager;
import exceptions.NotEnoughArgumentsException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongArgumentException;

import javax.naming.NoPermissionException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Execute all commands from the file
 */

public class ExecuteScript extends AbstractCommand implements IsNeedInput {
    private static int counter = 0;

    public ExecuteScript() {
        this.name = "execute_script file_name";
        this.description = "исполнить скрипт из указанного файла";
    }

    @Override
    public Response execute(CollectionManager collectionManager, ArgsShell args, UserData user) {
        collectionManager.addToHistory(this, user);
        Response response = new Response();
        for (Object data : args.getArguments()) {
            Request request = (Request) data;
            response.addData(request.getCommand().execute(collectionManager, request.getArguments(), user));
        }
        return response;
    }


    @Override
    public Object[] validate(String[] args) throws NotEnoughArgumentsException, WrongArgumentException {
        if (args.length < 2) throw new NotEnoughArgumentsException("Команда требует аргумент \"file_name\".");
        try {
            Path path = Paths.get(args[1]);
            if (!Files.exists(path)) throw new NoPermissionException("Файл " + path + " не существует.");
            if (!Files.isReadable(path)) throw new NoPermissionException("Не получается прочитать файл.");
            File file = new File(args[1]);

            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            ArrayList<Object> data = new ArrayList<>();
            while (true) {
                Request request = CommandManager.validateFromFile(bufferedReader);

                if (request == null) {
                    break;
                }
                data.add(request);
            }
            return data.toArray();
        } catch (NoPermissionException e) {
            throw new WrongArgumentException(e.getMessage());
        } catch (IOException e) {
            throw new WrongArgumentException("Не получилось прочитать файл.");
        }

    }

    @Override
    public Object[] validateFromFile(BufferedReader reader, String[] args) throws NotEnoughLinesException, IOException, NotEnoughArgumentsException, WrongArgumentException {
        if (args.length < 2) throw new NotEnoughArgumentsException("команда требует аргумент \"file_name\"");
        if (counter++ > 5)
            throw new NotEnoughLinesException("Превышено допустимое количество использований execute_script");

        return validate(args);
    }
    @Override
    public boolean isNeedInput(){
        return true;
    }
}
