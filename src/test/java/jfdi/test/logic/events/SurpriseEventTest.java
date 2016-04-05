// @@author A0130195M
package jfdi.test.logic.events;

import jfdi.logic.events.SurpriseEvent;
import jfdi.storage.apis.TaskAttributes;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Xinan
 */
public class SurpriseEventTest {

    @Mock
    private TaskAttributes task;

    @Test
    public void getTask() throws Exception {
        SurpriseEvent event = new SurpriseEvent(task);
        assertEquals(task, event.getTask());
    }

}
