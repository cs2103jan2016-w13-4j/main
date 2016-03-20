package jfdi.test.storage;

import static org.junit.Assert.*;

import java.time.Duration;
import java.util.HashSet;
import java.util.TreeSet;

import jfdi.storage.Constants;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.entities.Task;

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
        assertEquals(new Integer(1), task.getId());
        assertEquals(Constants.TEST_TASK_DESCRIPTION_1, task.getDescription());
        assertEquals(Constants.TEST_TASK_STARTDATETIME, task.getStartDateTime());
        assertEquals(Constants.TEST_TASK_ENDDATETIME, task.getEndDateTime());
        assertEquals(tags, task.getTags());
        assertEquals(reminders, task.getReminders());
        assertEquals(false, task.isCompleted());
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
        assertEquals(new Integer(1), task.getId());
        assertEquals(Constants.TEST_TASK_DESCRIPTION_2, task.getDescription());
        assertEquals(Constants.TEST_TASK_STARTDATETIME, task.getStartDateTime());
        assertEquals(Constants.TEST_TASK_ENDDATETIME, task.getEndDateTime());
        assertTrue(task.getTags().contains(Constants.TEST_TASK_TAG_1));
        assertTrue(task.getReminders().contains(Constants.TEST_TASK_REMINDER_DURATION_1));
        assertEquals(true, task.isCompleted());
    }

}
