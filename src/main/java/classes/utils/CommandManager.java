package classes.utils;

import classes.commands.*;
import classes.shells.ArgsShell;
import classes.shells.Response;
import exceptions.NotEnoughArgumentsException;
import exceptions.WrongArgumentException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Scanner;

public class CommandManager {
    private final HashMap<String, AbstractCommand> commands = new HashMap<>();
    private final HashMap<String, IsNeedInput> commandsWithArgs = new HashMap<>();

    public CommandManager() {
        commands.put("clear", new Clear());
        commands.put("execute_script", new ExecuteScript(commands));
        commands.put("exit", new Exit());
        commands.put("filter_less_than_group_admin", new FilterLessThanGroupAdmin());
        commands.put("filter_starts_with_name", new FilterStartsWithName());
        commands.put("help", new Help(commands));
        commands.put("history", new History());
        commands.put("info", new Info());
        commands.put("insert", new Insert());
        commands.put("print_unique_students_count", new PrintUniqueStudentsCount());
        commands.put("remove_key", new RemoveKey());
        commands.put("remove_lower", new RemoveLower());
        commands.put("remove_lower_key", new RemoveLowerKey());
        commands.put("save", new Save());
        commands.put("show", new Show());
        commands.put("update", new Update());

        commandsWithArgs.put("filter_less_than_group_admin", new FilterLessThanGroupAdmin());
        commandsWithArgs.put("filter_starts_with_name", new FilterStartsWithName());
        commandsWithArgs.put("insert", new Insert());
        commandsWithArgs.put("remove_key", new RemoveKey());
        commandsWithArgs.put("remove_lower", new RemoveLower());
        commandsWithArgs.put("remove_lower_key", new RemoveLowerKey());
        commandsWithArgs.put("update", new Update());

    }

    public void start(ObjectOutputStream out, ObjectInputStream in) {
        Scanner commandReader = new Scanner(System.in);
        while (true) {
            System.out.print("Введите команду: ");
            if (!commandReader.hasNextLine()) System.exit(0);
            String[] arguments = commandReader.nextLine().trim().toLowerCase().split("\\s+");
            try {
                String command = arguments[0].trim();
                ArgsShell args = new ArgsShell();
                if (command.equals("exit")){
                    commands.get(command).execute(new CollectionManager(), args);
                }

                if (commands.get(command).isNeedInput()) {
                    args.setArguments( commandsWithArgs.get(command).validate(arguments));
                    System.out.println(args.getArguments()[0]);
                }
                out.writeObject(commands.get(command));
                out.writeObject(args);
                out.flush();


                Response response = (Response) in.readObject();
                response.showData();



            } catch (NullPointerException e) {
                System.out.println("Не удалось обнаружить команду: " + arguments[0].trim());
            } catch (WrongArgumentException e) {
                System.out.println("Введён неподходящий аргумент! " + e.getMessage() + "Попробуйте еще раз!");
            } catch (NotEnoughArgumentsException e) {
                System.out.println("Недостаточно аргументов. " + e.getMessage() + "Попробуйте еще раз!");
            } catch (IOException e) {
                System.out.println("Не получилось отправить команду на сервер");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
