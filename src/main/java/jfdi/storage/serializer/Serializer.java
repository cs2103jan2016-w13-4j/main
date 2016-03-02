package jfdi.storage.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class Serializer {

    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new GsonBuilder().setPrettyPrinting().create();
        }
    }

    public static String serialize(Object source) {
        return gson.toJson(source);
    }

    public static <T> T deserialize(String persistedJsonData, Class<T> classOfT) {
        try {
            T resultantObject = gson.fromJson(persistedJsonData, classOfT);
            return resultantObject;
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

}
