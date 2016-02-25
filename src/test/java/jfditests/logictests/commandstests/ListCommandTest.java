package jfditests.logictests.commandstests;

import jfdi.logic.commands.ListCommand;
import jfdi.storage.records.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * @author Liu Xinan
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Task.class})
public class ListCommandTest {

    @Mock private Collection<Task> items;
    @Mock private Consumer<ListCommand> successHook;
    @Mock private Consumer<ListCommand> failureHook;

    private ListCommand listCommandWithTags = new ListCommand.Builder().addTag("test").build();
    private ListCommand listCommandWithoutTags = new ListCommand.Builder().build();

    @Before
    public void setUp() throws Exception {
        // Mocking Task
        mockStatic(Task.class);
        when(Task.getAll()).thenReturn(items);
    }

    @Test
    public void testGetItems() throws Exception {
        // Command must be executed before there is any items to be gotten.
        listCommandWithoutTags.execute();

        assertSame(items, listCommandWithoutTags.getItems());
    }

    @Test
    public void testGetTags() throws Exception {
        ArrayList<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        tags.add("tag3");

        ListCommand command = new ListCommand.Builder().addTags(tags).build();

        assertArrayEquals(tags.toArray(), command.getTags().toArray());
    }

    @Test
    public void testGetErrorType() throws Exception {
        assertNull(listCommandWithTags.getErrorType());

        listCommandWithTags.execute();
        assertEquals(listCommandWithTags.getErrorType(), ListCommand.ErrorType.UNKNOWN);
    }

    @Test
    public void testExecute() throws Exception {
        listCommandWithoutTags.execute();

        verifyStatic();
        Task.getAll();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetItemsBeforeExecute() throws Exception {
        ListCommand command = new ListCommand.Builder().build();
        command.getItems();
    }

    @Test
    public void testAddSuccessHookAndOnSuccess() throws Exception {
        ListCommand.addSuccessHook(successHook);
        listCommandWithoutTags.execute();

        verify(successHook).accept(listCommandWithoutTags);
    }

    @Test
    public void testAddFailureHookAndOnFailure() throws Exception {
        ListCommand.addFailureHook(failureHook);
        listCommandWithTags.execute();

        verify(failureHook).accept(listCommandWithTags);
    }

}
