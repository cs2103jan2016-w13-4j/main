//@@author A0121621Y

package jfdi.test.storage;

import static org.junit.Assert.*;

import jfdi.storage.Constants;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.entities.Task;

import org.junit.Test;

public class TaskTest {

    @Test
    public void testConstructorAndGetters() {
        // Generate the new Task
        Task task = new Task(
                1,
                Constants.TEST_TASK_DESCRIPTION_1,
                Constants.TEST_TASK_STARTDATETIME,
                Constants.TEST_TASK_ENDDATETIME,
                false
                );

        // Check for equivalence in all attributes
        assertEquals(new Integer(1), task.getId());
        assertEquals(Constants.TEST_TASK_DESCRIPTION_1, task.getDescription());
        assertEquals(Constants.TEST_TASK_STARTDATETIME, task.getStartDateTime());
        assertEquals(Constants.TEST_TASK_ENDDATETIME, task.getEndDateTime());
        assertEquals(false, task.isCompleted());
    }

    @Test
    public void testUpdate() {
        // Create a new task
        Task task = new Task(1, Constants.TEST_TASK_DESCRIPTION_1, null, null, false);

        // Update its attributes
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setId(1);
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_2);
        taskAttributes.setStartDateTime(Constants.TEST_TASK_STARTDATETIME);
        taskAttributes.setEndDateTime(Constants.TEST_TASK_ENDDATETIME);
        taskAttributes.setCompleted(true);
        task.update(taskAttributes);

        // Check if the new attributes are correct
        assertEquals(new Integer(1), task.getId());
        assertEquals(Constants.TEST_TASK_DESCRIPTION_2, task.getDescription());
        assertEquals(Constants.TEST_TASK_STARTDATETIME, task.getStartDateTime());
        assertEquals(Constants.TEST_TASK_ENDDATETIME, task.getEndDateTime());
        assertEquals(true, task.isCompleted());
    }

}
