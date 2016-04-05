// @@author A0130195M

package jfdi.test.logic.events;

import jfdi.logic.events.DeleteTaskDoneEvent;
import jfdi.storage.apis.TaskAttributes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class DeleteTaskDoneEventTest {

    @Mock
    private ArrayList<Integer> deletedIds;

    @Mock
    private ArrayList<TaskAttributes> deletedTasks;

    @Test
    public void getDeletedIds() throws Exception {
        DeleteTaskDoneEvent event = new DeleteTaskDoneEvent(deletedIds, deletedTasks);
        assertEquals(deletedIds, event.getDeletedIds());
    }

    @Test
    public void getDeletedTasks() throws Exception {
        DeleteTaskDoneEvent event = new DeleteTaskDoneEvent(deletedIds, deletedTasks);
        assertEquals(deletedTasks, event.getDeletedTasks());
    }
}
