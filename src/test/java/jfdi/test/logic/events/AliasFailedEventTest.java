package jfdi.test.logic.events;

import jfdi.logic.events.AliasFailedEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Xinan
 */
public class AliasFailedEventTest {

    private AliasFailedEvent event = new AliasFailedEvent("list", "ls", AliasFailedEvent.Error.DUPLICATED_ALIAS);

    @Test
    public void getCommand() throws Exception {
        assertEquals("list", event.getCommand());
    }

    @Test
    public void getAlias() throws Exception {
        assertEquals("ls", event.getAlias());
    }

    @Test
    public void getError() throws Exception {
        assertEquals(AliasFailedEvent.Error.DUPLICATED_ALIAS, event.getError());
    }
}
