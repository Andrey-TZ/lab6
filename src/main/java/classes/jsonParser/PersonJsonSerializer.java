package classes.jsonParser;

import exceptions.EmptyFieldException;
import exceptions.WrongFieldException;
import classes.model.Person;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class to serializing and deserializing elements of Person class
 */
public class PersonJsonSerializer implements JsonDeserializer<Person>, JsonSerializer<Person> {

    /**
     * Deserializing of element
     *
     * @param jsonElement the Json data being deserialized
     * @param type        the type of the Object to deserialize to
     * @return element of Coordinate class or null if data is incorrect
     * @throws JsonParseException if json is not in the expected format of typeofT
     */
    @Override
    public Person deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        String name = object.get("name").getAsString();
        try {
            Date birthday = new SimpleDateFormat("dd-MM-yyyy").parse(object.get("birthday").getAsString());
            Float height = object.get("height").getAsFloat();
            return new Person(1, name, birthday, height);
        } catch (ParseException e) {
            System.out.println("День Рождения человека введен не верно!");
            return null;
        } catch (WrongFieldException | EmptyFieldException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    /**
     * Serialize element to json object
     *
     * @param person the object that needs to be converted to Json.
     * @param type   the actual type of the source object
     * @return json object
     */
    @Override
    public JsonElement serialize(Person person, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("name", person.getName());
        String date = person.getBirthday();
        object.addProperty("birthday", date);
        object.addProperty("height", person.getHeight());


        return object;
    }
}
