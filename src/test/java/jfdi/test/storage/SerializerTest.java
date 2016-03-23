//@@author A0121621Y
package jfdi.test.storage;

import static org.junit.Assert.assertEquals;

import jfdi.storage.Constants;
import jfdi.storage.entities.Task;
import jfdi.storage.serializer.Serializer;

import org.junit.Test;

public class SerializerTest {

    @Test
    public void testSerializeAndDeserialize() {
        // Generate a new Task
        Task task = new Task(
                1,
                Constants.TEST_TASK_DESCRIPTION_1,
                Constants.TEST_TASK_STARTDATETIME,
                Constants.TEST_TASK_ENDDATETIME
                );

        // Serialize it
        String serializedJson = Serializer.serialize(task);

        // Deserialize the serialized JSON
        Task deserializedTask = Serializer.deserialize(serializedJson, Task.class);

        // Assert that the properties are still the same
        assertEquals(Constants.TEST_TASK_DESCRIPTION_1, deserializedTask.getDescription());
        assertEquals(Constants.TEST_TASK_STARTDATETIME, deserializedTask.getStartDateTime());
        assertEquals(Constants.TEST_TASK_ENDDATETIME, deserializedTask.getEndDateTime());
        assertEquals(false, deserializedTask.isCompleted());
    }

}
