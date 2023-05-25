package classes.utils;

import classes.jsonParser.JsonParser;
import classes.model.StudyGroup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;

public class FIleManager {
    private static final Path PATH = Paths.get("collection.json");

    public static boolean check() {
        return Files.exists(PATH) && Files.isReadable(PATH) && Files.isWritable(PATH);
    }

    public static void writeJson(Hashtable<Integer, StudyGroup> collection) {
        while (true) {
            try {
                if (check()) {
                    JsonParser.writeJson(collection, PATH);
                } else {
                    Files.createFile(PATH);
                }
            } catch (IOException e) {
                System.out.println("Введите новый путь к файлу с коллекцией: " );
            }
        }

    }

    public static Hashtable<Integer, StudyGroup> readJson() {
        while (true) {
            if(check()){
                return JsonParser.readJson(PATH);
            }
            else{
                try {
                    Files.createFile(PATH);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

}
