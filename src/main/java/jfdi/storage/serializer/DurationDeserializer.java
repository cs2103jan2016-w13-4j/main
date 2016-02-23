package jfdi.storage.serializer;

import java.lang.reflect.Type;
import java.time.Duration;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class DurationDeserializer implements JsonDeserializer<Duration> {

    @Override
    public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        String readableDurationString = json.getAsJsonPrimitive().getAsString();
        String durationString = "PT" + readableDurationString;
        return Duration.parse(durationString);
    }

}
