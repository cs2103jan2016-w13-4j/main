// @@author A0130195M
package jfdi.test.logic.events;

import jfdi.logic.events.UseDirectoryFailedEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Xinan
 */
public class UseDirectoryFailedEventTest {

    private String newDirectory = "!@#$!@#$";
    private UseDirectoryFailedEvent.Error error = UseDirectoryFailedEvent.Error.INVALID_PATH;

    @Test
    public void getNewDirectory() throws Exception {
        UseDirectoryFailedEvent event = new UseDirectoryFailedEvent(newDirectory, error);
        assertEquals(newDirectory, event.getNewDirectory());
    }

    @Test
    public void getError() throws Exception {
        UseDirectoryFailedEvent event = new UseDirectoryFailedEvent(newDirectory, error);
        assertEquals(error, event.getError());
    }

}
