package classes.jsonParser;

import exceptions.WrongFieldException;
import classes.model.Coordinates;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Class to serializing and deserializing elements of Coordinates class
 */
public class CoordinatesJsonSerializer implements JsonSerializer<Coordinates>, JsonDeserializer<Coordinates> {
    /**
     * Deserializing of element
     *
     * @param jsonElement the Json data being deserialized
     * @param type        the type of the Object to deserialize to
     * @return element of Coordinate class or null if data is incorrect
     * @throws JsonParseException if json is not in the expected format of typeofT
     */
    @Override
    public Coordinates deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        Float x = object.get("x").getAsFloat();
        float y = object.get("y").getAsFloat();
        try {
            return new Coordinates(x, y);
        } catch (WrongFieldException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Serialize element to json object
     *
     * @param coordinates the object that needs to be converted to Json.
     * @param type        the actual type of the source object
     * @return json object
     */
    @Override
    public JsonElement serialize(Coordinates coordinates, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("x", coordinates.getX());
        object.addProperty("y", coordinates.getY());
        return object;
    }
}
