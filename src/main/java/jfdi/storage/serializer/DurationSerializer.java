package jfdi.storage.serializer;

import java.lang.reflect.Type;
import java.time.Duration;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DurationSerializer implements JsonSerializer<Duration> {

    @Override
    public JsonElement serialize(Duration src, Type typeOfSrc, JsonSerializationContext context) {
        String durationString = src.toString();
        String readableDurationString = durationString.substring(2);
        return new JsonPrimitive(readableDurationString);
    }

}
