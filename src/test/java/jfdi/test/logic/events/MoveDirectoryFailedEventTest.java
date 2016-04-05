package jfdi.test.logic.events;

import jfdi.logic.events.MoveDirectoryFailedEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Xinan
 */
public class MoveDirectoryFailedEventTest {

    private String newDirectory = "!@#!@#$@#$";
    private MoveDirectoryFailedEvent.Error error = MoveDirectoryFailedEvent.Error.INVALID_PATH;

    @Test
    public void getNewDirectory() throws Exception {
        MoveDirectoryFailedEvent event = new MoveDirectoryFailedEvent(newDirectory, error);
        assertEquals(newDirectory, event.getNewDirectory());
    }

    @Test
    public void getError() throws Exception {
        MoveDirectoryFailedEvent event = new MoveDirectoryFailedEvent(newDirectory, error);
        assertEquals(error, event.getError());
    }

}
