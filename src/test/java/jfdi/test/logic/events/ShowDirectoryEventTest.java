// @@author A0130195M

package jfdi.test.logic.events;

import jfdi.logic.events.ShowDirectoryEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Xinan
 */
public class ShowDirectoryEventTest {

    private String pwd = "/home/ubuntu/jfdi";

    @Test
    public void getPwd() throws Exception {
        ShowDirectoryEvent event = new ShowDirectoryEvent(pwd);
        assertEquals(pwd, event.getPwd());
    }

}
