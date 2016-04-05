// @@author A0130195M

package jfdi.test.logic.events;

import jfdi.logic.events.UnaliasDoneEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Xinan
 */
public class UnaliasDoneEventTest {

    private String alias = "alias";

    @Test
    public void getAlias() throws Exception {
        UnaliasDoneEvent event = new UnaliasDoneEvent(alias);
        assertEquals(alias, event.getAlias());
    }

}
