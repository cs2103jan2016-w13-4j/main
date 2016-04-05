// @@author A0130195M
package jfdi.test.logic.events;

import jfdi.logic.events.SearchDoneEvent;
import jfdi.storage.apis.TaskAttributes;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

/**
 * @author Liu Xinan
 */
public class SearchDoneEventTest {

    @Mock
    private HashSet<String> keywords;

    @Mock
    private ArrayList<TaskAttributes> results;

    @Test
    public void getResults() throws Exception {
        SearchDoneEvent event = new SearchDoneEvent(results, keywords);
        assertEquals(results, event.getResults());
    }

    @Test
    public void getKeywords() throws Exception {
        SearchDoneEvent event = new SearchDoneEvent(results, keywords);
        assertEquals(keywords, event.getKeywords());
    }

}
