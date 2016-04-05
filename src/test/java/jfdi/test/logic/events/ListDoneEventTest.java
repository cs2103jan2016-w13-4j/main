// @@author A0130195M
package jfdi.test.logic.events;

import jfdi.logic.commands.ListCommand;
import jfdi.logic.events.ListDoneEvent;
import jfdi.storage.apis.TaskAttributes;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Xinan
 */
public class ListDoneEventTest {

    @Mock
    private ListCommand.ListType listType;

    @Mock
    private ArrayList<TaskAttributes> items;

    @Test
    public void getItems() throws Exception {
        ListDoneEvent event = new ListDoneEvent(listType, items);
        assertEquals(items, event.getItems());
    }

    @Test
    public void getListType() throws Exception {
        ListDoneEvent event = new ListDoneEvent(listType, items);
        assertEquals(listType, event.getListType());
    }

}
