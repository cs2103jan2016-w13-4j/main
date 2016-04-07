package jfdi.test.logic.commands;

import jfdi.logic.commands.SearchCommand;
import jfdi.storage.apis.TaskAttributes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class SearchCommandTest extends CommonCommandTest {

    @Test
    public void testBuilder() throws Exception {
        ArrayList<String> keywords = new ArrayList<>();
        keywords.add("you");
        keywords.add("up");

        SearchCommand command = new SearchCommand.Builder()
            .addKeyword("never")
            .addKeyword("gonna")
            .addKeyword("give")
            .addKeywords(keywords)
            .build();

        assertTrue(command.getKeywords().contains("never"));
        assertTrue(command.getKeywords().contains("gonna"));
        assertTrue(command.getKeywords().contains("give"));
        assertTrue(command.getKeywords().contains("you"));
        assertTrue(command.getKeywords().contains("up"));
        assertNull(command.getResults());
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        TaskAttributes neverGonnaGiveYouUp = new TaskAttributes();
        neverGonnaGiveYouUp.setDescription("Never gonna give you up");
        TaskAttributes never = new TaskAttributes();
        never.setDescription("never");
        TaskAttributes gonna = new TaskAttributes();
        gonna.setDescription("gonna");
        TaskAttributes give = new TaskAttributes();
        give.setDescription("give");
        TaskAttributes you = new TaskAttributes();
        you.setDescription("you");
        TaskAttributes up = new TaskAttributes();
        up.setDescription("up");

        ArrayList<TaskAttributes> tasks = new ArrayList<>();
        tasks.add(neverGonnaGiveYouUp);
        tasks.add(never);
        tasks.add(gonna);
        tasks.add(give);
        tasks.add(you);
        tasks.add(up);

        when(taskDb.getAll()).thenReturn(tasks);
    }

    @Test
    public void testExecute_singleKeyword() throws Exception {
        SearchCommand command = new SearchCommand.Builder()
            .addKeyword("never")
            .build();

        command.execute();

        assertEquals(2, command.getResults().size());
    }

    @Test
    public void testExecute_multipleKeyword() throws Exception {
        SearchCommand command = new SearchCommand.Builder()
            .addKeyword("never")
            .addKeyword("gonna")
            .addKeyword("give")
            .addKeyword("you")
            .addKeyword("up")
            .build();

        command.execute();

        assertEquals(6, command.getResults().size());
        assertEquals("Never gonna give you up", command.getResults().get(0).getDescription());
    }

    @Test
    public void testUndo() throws Exception {
        SearchCommand command = new SearchCommand.Builder()
            .addKeyword("never")
            .addKeyword("gonna")
            .addKeyword("give")
            .build();

        thrown.expect(AssertionError.class);
        command.undo();
    }

}
