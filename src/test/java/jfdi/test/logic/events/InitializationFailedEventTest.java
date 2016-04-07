// @@author A0130195M

package jfdi.test.logic.events;

import jfdi.logic.events.InitializationFailedEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Xinan
 */
public class InitializationFailedEventTest {

    private InitializationFailedEvent.Error error = InitializationFailedEvent.Error.INVALID_PATH;
    private String path = "!@#!@#!@#";

    @Test
    public void getError() throws Exception {
        InitializationFailedEvent event = new InitializationFailedEvent(error, path);
        assertEquals(error, event.getError());
    }

    @Test
    public void getPath() throws Exception {
        InitializationFailedEvent event = new InitializationFailedEvent(error, path);
        assertEquals(path, event.getPath());
    }

    @Test
    public void testEnum() throws Exception {
        for (InitializationFailedEvent.Error error : InitializationFailedEvent.Error.values()) {
            // Force a full coverage on enums
            InitializationFailedEvent.Error.valueOf(error.toString());
        }
    }
}
