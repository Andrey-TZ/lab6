package classes.jsonParser;

import classes.model.Coordinates;
import classes.model.StudyGroup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.naming.NoPermissionException;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Scanner;

public class JsonParser {
    private static Path path;
    private final Type collectionType = new TypeToken<Hashtable<Integer, StudyGroup>>() {
    }.getType();

    /**
     * Request path to file
     *
     * @return path to file
     */
    private Path requestFilePath() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.print("Введите путь к файлу с коллекцией: ");
                if (!scanner.hasNextLine()) System.exit(0);
                path = Paths.get(scanner.nextLine());
                if (!Files.exists(path)) throw new FileNotFoundException("Файл \"" + path + "\" не найден");
                if (!Files.isReadable(path)) throw new NoPermissionException("Нет получается прочитать " + path);
                if (!Files.isWritable(path)) throw new NoPermissionException("Не получается записать данные в " + path);
                return path;
            } catch (NoPermissionException | FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    /**
     * Checking is file exists, readable, writable.
     * And if it's not - request new path
     *
     * @param path path to file
     * @return path to file
     */
    private Boolean checkPath(Path path) {
        try {

            if (!Files.isReadable(path)) throw new NoPermissionException("Не получается прочитать файл");
            if (!Files.isWritable(path))
                throw new NoPermissionException("Не получается записать данные в этот файл");
            return true;
        } catch (NoPermissionException e) {
            System.out.println("Нет доступа к " + path + " - " + e.getMessage());
            return false;
        }

    }

    /**
     * Reading file
     *
     * @param path path to file
     * @return file data in char[]
     */
    public char[] read(Path path) {
        InputStreamReader reader;
        File file;

        while (true) {
            try {
                file = new File(path.toUri());
                reader = new InputStreamReader(new FileInputStream(file));
                char[] fileContent = new char[(int) file.length()];
                reader.read(fileContent);
                reader.close();
                return fileContent;
            } catch (IOException e) {
                System.out.println("Не получилось прочитать файл");
                path = requestFilePath();
            }
        }
    }

    /**
     * Deserializing json
     *
     * @return Hashtable of StudyGroup objects
     */
    public Hashtable<Integer, StudyGroup> collectionFromJson(String[] args) {

        if (args.length != 0) {
            path = Paths.get(args[0]);
        } else {
            System.out.println("Пожалуйста, укажите путь к файлу");
            path = requestFilePath();
        }

        char[] fileContent = read(path);
        String jsonString = String.valueOf(fileContent);

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(collectionType, new HashTableSerializer());
        Gson gson = builder.create();

        return gson.fromJson(jsonString, collectionType);
    }

    /**
     * Serializing collection
     *
     * @param collection collection of StudyGroup
     */
    public void writeJson(Hashtable<Integer, StudyGroup> collection) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(StudyGroup.class, new StudyGroupJsonSerializer());
        builder.registerTypeAdapter(Coordinates.class, new CoordinatesJsonSerializer());
        Gson gson = builder.create();
        String collectionJsonString = gson.toJson(collection, collectionType);
        writeToFile(collectionJsonString);
    }

    /**
     * Writing String to file
     *
     * @param jsonString string of serialized collection
     */
    private void writeToFile(String jsonString) {
        while (true) {
            try {
                if (!Files.exists(path)) Files.createFile(path);
                PrintWriter out = new PrintWriter(new FileWriter(path.toFile()));
                out.write(jsonString);
                out.close();
                break;
            } catch (IOException e) {
                System.out.println("не удалось записать данные в этот файл. Введите путь к другому файлу!");
                path = requestFilePath();
            }
        }
    }
}
