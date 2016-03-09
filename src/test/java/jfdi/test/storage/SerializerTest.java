package jfdi.test.storage;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.HashSet;
import java.util.TreeSet;

import jfdi.storage.Constants;
import jfdi.storage.entities.Task;
import jfdi.storage.serializer.Serializer;

import org.junit.Test;

public class SerializerTest {

    @Test
    public void testSerializeAndDeserialize() {
        // Prepare the tags and reminders
        HashSet<String> tags = new HashSet<String>();
        tags.add(Constants.TEST_TASK_TAG_1);
        TreeSet<Duration> reminders = new TreeSet<Duration>();
        reminders.add(Constants.TEST_TASK_REMINDER_DURATION_1);

        // Generate a new Task
        Task task = new Task(
                1,
                Constants.TEST_TASK_DESCRIPTION_1,
                Constants.TEST_TASK_STARTDATETIME,
                Constants.TEST_TASK_ENDDATETIME,
                tags,
                reminders
                );

        // Serialize it
        String serializedJson = Serializer.serialize(task);

        // Deserialize the serialized JSON
        Task deserializedTask = Serializer.deserialize(serializedJson, Task.class);

        // Check if the properties are still the same
        assertEquals(deserializedTask.getDescription(), Constants.TEST_TASK_DESCRIPTION_1);
        assertEquals(deserializedTask.getStartDateTime(), Constants.TEST_TASK_STARTDATETIME);
        assertEquals(deserializedTask.getEndDateTime(), Constants.TEST_TASK_ENDDATETIME);
        assertEquals(deserializedTask.getTags(), tags);
        assertEquals(deserializedTask.getReminders(), reminders);
        assertEquals(deserializedTask.isCompleted(), false);
    }

}
