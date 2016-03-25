package jfdi.test.logic.events;

import jfdi.logic.events.UnaliasFailEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Xinan
 */
public class UnaliasFailEventTest {

    private String alias = "alias";
    private UnaliasFailEvent.Error error = UnaliasFailEvent.Error.NON_EXISTENT_ALIAS;
    private UnaliasFailEvent event = new UnaliasFailEvent(alias, error);

    public void getAlias() throws Exception {
        assertEquals(alias, event.getAlias());
    }

    @Test
    public void getError() throws Exception {
        assertEquals(error, event.getError());
    }

}
