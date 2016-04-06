package jfdi.test.logic.events;

import jfdi.logic.events.ExitCalledEvent;
import org.junit.Test;

import static org.junit.Assert.*;

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
