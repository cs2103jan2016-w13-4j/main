// @@author A0130195M
package jfdi.test.logic.events;

import jfdi.logic.events.HelpRequestedEvent;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Liu Xinan
 */
public class HelpRequestedEventTest {

    @Test
    public void nothingToTest() {
        HelpRequestedEvent event = new HelpRequestedEvent();
        assertNotNull(event);
    }

}
