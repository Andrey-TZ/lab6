package classes.jsonParser;


import classes.model.StudyGroup;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * Class to serializing and deserializing Hashtable<Integer, StudyGroup> Collection
 */

public class HashTableSerializer implements JsonDeserializer<Hashtable<Integer, StudyGroup>> {
    private GsonBuilder builder = new GsonBuilder();
    private Gson gson;

    {
        builder.registerTypeAdapter(StudyGroup.class, new StudyGroupJsonSerializer());
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
    @Override
    public Hashtable<Integer, StudyGroup> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Hashtable<Integer, StudyGroup> groups = new Hashtable<>();
        JsonObject object = jsonElement.getAsJsonObject();
        Map<String, JsonElement> groupMap = object.asMap();
        Set<String> keys = groupMap.keySet();
        for (String s : keys) {

            StudyGroup group = gson.fromJson(groupMap.get(s).getAsJsonObject(), StudyGroup.class);
            if (group != null) {
                groups.put(Integer.parseInt(s), group);
            } else System.out.println("элемент с ключом " + s + " не удалось прочитать");
        }
        return groups;
    }
}
