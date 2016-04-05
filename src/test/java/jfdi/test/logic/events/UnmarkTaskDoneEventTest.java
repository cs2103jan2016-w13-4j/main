// @@author A0130195M
package jfdi.test.logic.events;

import jfdi.logic.events.UnmarkTaskDoneEvent;
import jfdi.storage.apis.TaskAttributes;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Xinan
 */
public class UnmarkTaskDoneEventTest {

    @Mock
    private ArrayList<Integer> screenIds;

    @Mock
    private ArrayList<TaskAttributes> unmarkedTasks;

    @Test
    public void getScreenIds() throws Exception {
        UnmarkTaskDoneEvent event = new UnmarkTaskDoneEvent(screenIds, unmarkedTasks);
        assertEquals(screenIds, event.getScreenIds());
    }

    @Test
    public void getUnmarkedTasks() throws Exception {
        UnmarkTaskDoneEvent event = new UnmarkTaskDoneEvent(screenIds, unmarkedTasks);
        assertEquals(unmarkedTasks, event.getUnmarkedTasks());
    }

}
