package classes.commands;

import classes.dataBase.UserData;
import classes.shells.ArgsShell;
import classes.shells.Response;
import exceptions.NotEnoughArgumentsException;
import exceptions.NotEnoughLinesException;
import exceptions.WrongArgumentException;
import classes.utils.CollectionManager;

import java.io.BufferedReader;
import java.util.Set;

/**
 * Command to display all unique values of students_count param
 */

public class PrintUniqueStudentsCount extends AbstractCommand {
    public PrintUniqueStudentsCount() {
        this.name = "print_unique_students_count";
        this.description = "вывести уникальные значения \"students count\"";
    }


    @Override
    public Response execute(CollectionManager collectionManager, ArgsShell args, UserData user) {
        Response response = new Response("Все уникальные значения \"students count\": ");
        int index = 0;
        Set<Long> uniqueCounts = collectionManager.getUniqueStudentsCount();
        StringBuilder output = new StringBuilder();
        for (Long count : uniqueCounts) {
            output.append(count.toString());
            if (++index < uniqueCounts.size()) {
                output.append(",");
            }
            output.append(" ");
        }
        response.setData(output.toString());
        collectionManager.addToHistory(this, user);
        return response;
    }
}
