package jfdi.test.logic.events;

import jfdi.logic.events.AddTaskDoneEvent;
import jfdi.storage.apis.TaskAttributes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 * @author Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class AddTaskDoneEventTest {

    @Mock
    private TaskAttributes task;

    @Test
    public void getTask() throws Exception {
        AddTaskDoneEvent e = new AddTaskDoneEvent(task);
        assertSame(task, e.getTask());
    }

}
