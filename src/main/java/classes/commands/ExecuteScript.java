package classes.commands;

import classes.shells.ArgsShell;
import classes.shells.Request;
import classes.shells.Response;
import classes.utils.CommandManager;
import exceptions.NotEnoughArgumentsException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongArgumentException;
import classes.utils.CollectionManager;

import javax.naming.NoPermissionException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Execute all commands from the file
 */

public class ExecuteScript extends AbstractCommand implements IsNeedInput {
    private static int counter = 0;

    public ExecuteScript() {

        this.name = "execute_script file_name";
        this.description = "исполнить скрипт из указанного файла";
    }


    public void execute(String[] args, CollectionManager collectionManager) throws NotEnoughArgumentsException, WrongArgumentException {
        if (args.length < 2) throw new NotEnoughArgumentsException("команда требует аргумент \"file_name\"");

        collectionManager.addToHistory(this);

    }

    @Override
    public Response execute(CollectionManager collectionManager, ArgsShell args)  {
        Response response = new Response();
        for(Object data: args.getArguments()){
            Request request = (Request) data;
            response.addData(request.getCommand().execute(collectionManager, request.getArguments()));
        }
        return response;
    }

    @Override
    public void executeFromFile(BufferedReader reader, String[] args, CollectionManager collectionManager) throws NotEnoughLinesException, WrongArgumentException, NotEnoughArgumentsException {
        if(counter >=5 ) throw new NotEnoughLinesException("Превышено допустимое количество использований execute_script");
        counter++;
        execute(args, collectionManager);
    }

    @Override
    public Object[] validate(String[] args) throws NotEnoughArgumentsException, WrongArgumentException {
        if (args.length < 2) throw new NotEnoughArgumentsException("команда требует аргумент \"file_name\"");
        try {
            Path path = Paths.get(args[1]);
            if(!Files.isReadable(path)) throw new NoPermissionException("Не получается прочитать файл");
            File file = new File(args[1]);

            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            ArrayList<Object> data = new ArrayList<>();
            while (true){
                Request request = CommandManager.validateFromFile(bufferedReader);
                if (request == null){
                    break;
                }
                data.add(request);
            }
            return new ArrayList[]{data};

        }
        catch (FileNotFoundException e){
            throw new WrongArgumentException("Файл с именем \"" + args[1] + "\" ней найден");
        }
        catch (NoPermissionException e) {
            throw new WrongArgumentException(e.getMessage());
        } catch (IOException e) {
            throw new WrongArgumentException("Не получилось прочитать файл, попробуйте с другим файлом");
        }

    }

    @Override
    public Object[] validateFromFile(BufferedReader reader, String[] args) throws NotEnoughLinesException, IOException, NotEnoughArgumentsException, WrongArgumentException {
        return new Object[0];
    }
}
