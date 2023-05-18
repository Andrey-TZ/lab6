package classes.commands;

import classes.shells.ArgsShell;
import classes.shells.Response;
import exceptions.NotEnoughArgumentsException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongArgumentException;
import classes.utils.CollectionManager;

import java.io.*;
import java.util.HashMap;

/**
 * Execute all commands from the file
 */

public class ExecuteScript extends AbstractCommand {
    private HashMap<String, AbstractCommand> commands;
    private static int counter = 0;

    public ExecuteScript(HashMap<String, AbstractCommand> commands) {
        this.commands = commands;
        this.name = "execute_script file_name";
        this.description = "исполнить скрипт из указанного файла";
    }


    public void execute(String[] args, CollectionManager collectionManager) throws NotEnoughArgumentsException, WrongArgumentException {
        if (args.length < 2) throw new NotEnoughArgumentsException("команда требует аргумент \"file_name\"");

        try {
            File file = new File(args[1]);
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();

            while (line != null) {
                String[] command = line.split("\\s+");
                try {
                    commands.get(command[0]).executeFromFile(bufferedReader, command, collectionManager);

                } catch (NullPointerException e) {
                    System.out.println("Не удалось найти команду " + command[0]);
                } catch (NotEnoughLinesException e) {
                    System.out.println(e.getMessage());
                }
                catch(WrongArgumentException e){
                    System.out.println("Введён неподходящий аргумент! " + e.getMessage() + "Попробуйте еще раз!");
                }
                catch (NotEnoughArgumentsException e){
                    System.out.println("Недостаточно аргументов. " + e.getMessage() + "Попробуйте еще раз!");
                }
                line = bufferedReader.readLine();
            }
            System.out.println("Все команды из файла выполнены!");
        } catch (FileNotFoundException e) {
            System.out.println("Файл \"" + args[1] + "\" не найден");
        } catch (IOException e) {
            System.out.println("Не получилось прочитать файл, попробуйте с другим файлом");
        }
        collectionManager.addToHistory(this);

    }

    @Override
    public Response execute(CollectionManager collectionManager, ArgsShell args)  {
        return null;
    }

    @Override
    public void executeFromFile(BufferedReader reader, String[] args, CollectionManager collectionManager) throws NotEnoughLinesException, WrongArgumentException, NotEnoughArgumentsException {
        if(counter >=5 ) throw new NotEnoughLinesException("Превышено допустимое количество использований execute_script");
        counter++;
        execute(args, collectionManager);
    }

}
