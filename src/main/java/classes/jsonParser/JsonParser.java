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
    private static final Type collectionType = new TypeToken<Hashtable<Integer, StudyGroup>>() {
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
     * Deserializing json
     *
     * @return Hashtable of StudyGroup objects
     */
    public static Hashtable<Integer, StudyGroup> readJson(Path path) {
        File file = new File(path.toUri());
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            char[] fileContent = new char[(int) file.length()];
            reader.read(fileContent);
            reader.close();
            String jsonString = String.valueOf(fileContent);

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(collectionType, new HashTableSerializer());
            Gson gson = builder.create();

            return gson.fromJson(jsonString, collectionType);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * Serializing collection
     *
     * @param collection collection of StudyGroup
     */
    public static void writeJson(Hashtable<Integer, StudyGroup> collection, Path file) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(StudyGroup.class, new StudyGroupJsonSerializer());
        builder.registerTypeAdapter(Coordinates.class, new CoordinatesJsonSerializer());
        Gson gson = builder.create();
        String collectionJsonString = gson.toJson(collection, collectionType);

        PrintWriter out = new PrintWriter(new FileWriter(file.toFile()));
        out.write(collectionJsonString);
        out.close();
    }

}
