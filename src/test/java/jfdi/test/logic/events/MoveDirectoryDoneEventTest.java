package jfdi.test.logic.events;

import jfdi.logic.events.MoveDirectoryDoneEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Xinan
 */
public class MoveDirectoryDoneEventTest {

    private String newDirectory = ".";

    @Test
    public void getNewDirectory() throws Exception {
        MoveDirectoryDoneEvent event = new MoveDirectoryDoneEvent(newDirectory);
        assertEquals(newDirectory, event.getNewDirectory());
    }

}
