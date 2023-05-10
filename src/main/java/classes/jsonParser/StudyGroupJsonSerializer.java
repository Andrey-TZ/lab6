package classes.jsonParser;

import exceptions.EmptyFieldException;
import exceptions.WrongFieldException;
import classes.model.*;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.DateTimeException;
import java.time.LocalDateTime;


/**
 * Class to serializing and deserializing elements of StudyGroup class
 */
public class StudyGroupJsonSerializer implements JsonDeserializer<StudyGroup>, JsonSerializer<StudyGroup> {
    private GsonBuilder builder = new GsonBuilder();
    private Gson gson;

    {
        builder.registerTypeAdapter(Coordinates.class, new CoordinatesJsonSerializer());
        builder.registerTypeAdapter(Person.class, new PersonJsonSerializer());
        gson = builder.create();
    }

    /**
     * Deserializing of element
     *
     * @param jsonElement the Json data being deserialized
     * @param type        the type of the Object to deserialize to
     * @return element of Coordinate class or null if data is incorrect
     * @throws JsonParseException if json is not in the expected format of typeofT
     */
    public StudyGroup deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
        JsonObject object = jsonElement.getAsJsonObject();

        int id = object.get("id").getAsInt();
        String name = object.get("name").getAsString();
        Coordinates coordinates;
        LocalDateTime creationDate;
        long studentsCount;
        FormOfEducation formOfEducation;
        Semester semester;
        Person groupAdmin;

        if (object.get("coordinates").isJsonObject()) {
            coordinates = gson.fromJson(object.get("coordinates").getAsJsonObject(), Coordinates.class);
        } else coordinates = null;

        try {
            creationDate = LocalDateTime.parse(object.get("date").getAsString());
        } catch (DateTimeException e) {
            System.out.print("\"Неверный формат даты\" - ");
            return null;
        }

        try {
            studentsCount = object.get("studentsCount").getAsLong();
        } catch (NumberFormatException e) {
            System.out.print("\"Некорректно введено количество учеников\" - ");
            return null;
        }

        try {
            formOfEducation = FormOfEducation.valueOf(object.get("formOfEducation").getAsString());
        } catch (IllegalArgumentException e) {
            System.out.print("\"Некорректная форма обучения\" - ");
            return null;
        }

        try {
            semester = Semester.valueOf(object.get("semesterEnum").getAsString());
        } catch (IllegalArgumentException e) {
            semester = null;
        }


        if (object.get("groupAdmin").isJsonObject()) {
            groupAdmin = gson.fromJson(object.get("groupAdmin").getAsJsonObject(), Person.class);
        } else {
            groupAdmin = null;
        }

        try {
            return new StudyGroup(id, name, coordinates, creationDate, studentsCount, formOfEducation, semester, groupAdmin);
        } catch (WrongFieldException | EmptyFieldException e) {
            System.out.print("\"" + e.getMessage() + "\" - ");
            return null;
        }
    }

    /**
     * Serialize element to json object
     *
     * @param studyGroup the object that needs to be converted to Json.
     * @param type       the actual type of the source object
     * @return json object
     */
    @Override
    public JsonElement serialize(StudyGroup studyGroup, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();


        object.addProperty("id", studyGroup.getId());
        object.addProperty("name", studyGroup.getName());
        object.add("coordinates", gson.toJsonTree(studyGroup.getCoordinates(), Coordinates.class));
        object.addProperty("date", studyGroup.getCreationDate());
        object.addProperty("studentsCount", studyGroup.getStudentsCount());
        object.add("formOfEducation", gson.toJsonTree(studyGroup.getFormOfEducation()));

        if (studyGroup.getSemesterEnum() == (null)) {
            object.addProperty("semesterEnum", "null");
        } else {
            object.add("semesterEnum", gson.toJsonTree(studyGroup.getSemesterEnum(), Semester.class));
        }

        if (studyGroup.getGroupAdmin() == (null)) {
            object.addProperty("groupAdmin", "null");
        } else {
            object.add("groupAdmin", gson.toJsonTree(studyGroup.getGroupAdmin()));
        }
        return object;
    }
}
