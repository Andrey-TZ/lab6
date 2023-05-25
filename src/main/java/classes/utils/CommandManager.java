package classes.utils;

import classes.commands.*;
import classes.shells.ArgsShell;
import classes.shells.Request;
import classes.shells.Response;
import exceptions.NotEnoughArgumentsException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongArgumentException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Scanner;

public class CommandManager {
    private static final HashMap<String, AbstractCommand> commands = new HashMap<>();
    private static final HashMap<String, IsNeedInput> commandsWithArgs = new HashMap<>();

    public CommandManager() {
        commands.put("clear", new Clear());
        commands.put("execute_script", new ExecuteScript());
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

    public Request start() {
        Scanner commandReader = new Scanner(System.in);
        while (true) {
            System.out.print("Введите команду: ");
            if (!commandReader.hasNextLine()) System.exit(0);
            String[] arguments = commandReader.nextLine().trim().toLowerCase().split("\\s+");
            try {
                String command = arguments[0].trim();
                ArgsShell args = new ArgsShell();
                if (command.equals("exit")) {
                    commands.get(command).execute(new CollectionManager(), args);
                }

                if (commands.get(command).isNeedInput()) {
                    args.setArguments(commandsWithArgs.get(command).validate(arguments));
                }
                return new Request(commands.get(command), args);
            } catch (NullPointerException e) {
                System.out.println("Не удалось обнаружить команду: " + arguments[0].trim());
            } catch (WrongArgumentException e) {
                System.out.println("Введён неподходящий аргумент! " + e.getMessage() + "Попробуйте еще раз!");
            } catch (NotEnoughArgumentsException e) {
                System.out.println("Недостаточно аргументов. " + e.getMessage() + "Попробуйте еще раз!");
            }

        }
    }

    public static Request validateFromFile(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        while (line != null) {
            String[] argsFromFile = line.split("\\s+");
            AbstractCommand command = commands.get(argsFromFile[0]);
            try {
                ArgsShell argsToRequest = new ArgsShell();
                if (command.isNeedInput()) {
                    argsToRequest.setArguments(commandsWithArgs.get(command).validateFromFile(reader, argsFromFile));
                }
                return new Request(commands.get(command), argsToRequest);
            } catch (NullPointerException e) {
                System.out.println("Не удалось найти команду " + argsFromFile[0]);
            } catch (NotEnoughLinesException e) {
                System.out.println(e.getMessage());
            } catch (WrongArgumentException e) {
                System.out.println("Введён неподходящий аргумент! " + e.getMessage() + "Попробуйте еще раз!");
            } catch (NotEnoughArgumentsException e) {
                System.out.println("Недостаточно аргументов. " + e.getMessage() + "Попробуйте еще раз!");
            }
            line = reader.readLine();
        }
        return null;
    }
}
