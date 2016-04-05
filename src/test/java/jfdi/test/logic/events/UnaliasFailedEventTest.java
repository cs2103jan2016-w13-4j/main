package jfdi.test.logic.events;

import jfdi.logic.events.UnaliasFailedEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Xinan
 */
public class UnaliasFailedEventTest {

    private String alias = "alias";
    private UnaliasFailedEvent.Error error = UnaliasFailedEvent.Error.NON_EXISTENT_ALIAS;
    private UnaliasFailedEvent event = new UnaliasFailedEvent(alias, error);

    @Test
    public void getAlias() throws Exception {
        assertEquals(alias, event.getAlias());
    }

    @Test
    public void getError() throws Exception {
        assertEquals(error, event.getError());
    }

}
