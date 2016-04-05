package jfdi.test.logic.events;

import jfdi.logic.events.MarkTaskDoneEvent;
import jfdi.storage.apis.TaskAttributes;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * @@author Liu Xinan
 */
public class MarkTaskDoneEventTest {

    @Mock
    private ArrayList<Integer> screenIds;

    @Mock
    private ArrayList<TaskAttributes> markedTasks;

    @Test
    public void getScreenIds() throws Exception {
        MarkTaskDoneEvent event = new MarkTaskDoneEvent(screenIds, markedTasks);
        assertEquals(screenIds, event.getScreenIds());
    }

    @Test
    public void getMarkedTasks() throws Exception {
        MarkTaskDoneEvent event = new MarkTaskDoneEvent(screenIds, markedTasks);
        assertEquals(markedTasks, event.getMarkedTasks());
    }

}
