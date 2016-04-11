// @@author A0130195M

package jfdi.test.logic.events;

import jfdi.logic.events.ExitCalledEvent;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author Xinan
 */
public class ExitCalledEventTest {

    @Test
    public void nothingToTest() {
        ExitCalledEvent event = new ExitCalledEvent();
        assertNotNull(event);
    }

}
