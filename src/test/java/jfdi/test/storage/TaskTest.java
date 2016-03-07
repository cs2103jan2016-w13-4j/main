package jfdi.test.storage;

import static org.junit.Assert.*;

import java.time.Duration;
import java.util.HashSet;
import java.util.TreeSet;

import jfdi.storage.Constants;
import jfdi.storage.data.Task;
import jfdi.storage.data.TaskAttributes;

import org.junit.Test;

public class TaskTest {

    @Test
    public void testConstructorAndGetters() {
        // Prepare the tags and reminders
        HashSet<String> tags = new HashSet<String>();
        tags.add(Constants.TEST_TASK_TAG_1);
        TreeSet<Duration> reminders = new TreeSet<Duration>();
        reminders.add(Constants.TEST_TASK_REMINDER_DURATION_1);

        // Generate the new Task
        Task task = new Task(
                1,
                Constants.TEST_TASK_DESCRIPTION_1,
                Constants.TEST_TASK_STARTDATETIME,
                Constants.TEST_TASK_ENDDATETIME,
                tags,
                reminders
                );

        // Check for equivalence in all attributes
        assertEquals(task.getId(), new Integer(1));
        assertEquals(task.getDescription(), Constants.TEST_TASK_DESCRIPTION_1);
        assertEquals(task.getStartDateTime(), Constants.TEST_TASK_STARTDATETIME);
        assertEquals(task.getEndDateTime(), Constants.TEST_TASK_ENDDATETIME);
        assertEquals(task.getTags(), tags);
        assertEquals(task.getReminders(), reminders);
        assertEquals(task.isCompleted(), false);
    }

    @Test
    public void testUpdate() {
        // Create a new task
        Task task = new Task(1, Constants.TEST_TASK_DESCRIPTION_1, null, null, null, null);

        // Update its attributes
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setId(1);
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_2);
        taskAttributes.setStartDateTime(Constants.TEST_TASK_STARTDATETIME);
        taskAttributes.setEndDateTime(Constants.TEST_TASK_ENDDATETIME);
        taskAttributes.setTags(Constants.TEST_TASK_TAG_1);
        taskAttributes.setReminders(Constants.TEST_TASK_REMINDER_DURATION_1);
        taskAttributes.setCompleted(true);
        task.update(taskAttributes);

        // Check if the new attributes are correct
        assertEquals(task.getId(), new Integer(1));
        assertEquals(task.getDescription(), Constants.TEST_TASK_DESCRIPTION_2);
        assertEquals(task.getStartDateTime(), Constants.TEST_TASK_STARTDATETIME);
        assertEquals(task.getEndDateTime(), Constants.TEST_TASK_ENDDATETIME);
        assertTrue(task.getTags().contains(Constants.TEST_TASK_TAG_1));
        assertTrue(task.getReminders().contains(Constants.TEST_TASK_REMINDER_DURATION_1));
        assertEquals(task.isCompleted(), true);
    }

}
