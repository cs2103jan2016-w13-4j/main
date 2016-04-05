package jfdi.test.logic.events;

import jfdi.logic.events.UseDirectoryDoneEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Xinan
 */
public class UseDirectoryDoneEventTest {

    private String newDirectory = ".";

    @Test
    public void getNewDirectory() throws Exception {
        UseDirectoryDoneEvent event = new UseDirectoryDoneEvent(newDirectory);
        assertEquals(newDirectory, event.getNewDirectory());
    }

}
