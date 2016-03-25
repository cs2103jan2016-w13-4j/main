//@@author A0121621Y

package jfdi.storage.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * This is the Serializer used by the Storage component. It is extracted into a
 * class by itself to allow for global extensions and modifications.
 *
 * @author Thng Kai Yuan
 *
 */
public class Serializer {

    private static Gson gson = null;

    /**
     * The static constructor. We can configure the settings for the GSON
     * builder here.
     */
    static {
        if (gson == null) {
            gson = new GsonBuilder().setPrettyPrinting().create();
        }
    }

    /**
     * Turns an object into its JSON form.
     *
     * @param source
     *            the object that we want to serialize
     * @return the serialized string of the object
     */
    public static String serialize(Object source) {
        return gson.toJson(source);
    }

    /**
     * Transforms a JSON string into the object that it represents.
     *
     * @param persistedJsonData
     *            the JSON string
     * @param classOfT
     *            the class of the object that the JSON string represents
     * @return the deserialized object
     */
    public static <T> T deserialize(String persistedJsonData, Class<T> classOfT) {
        try {
            T resultantObject = gson.fromJson(persistedJsonData, classOfT);
            return resultantObject;
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

}
