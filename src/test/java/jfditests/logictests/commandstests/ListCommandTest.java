package jfditests.logictests.commandstests;

import jfdi.logic.commands.ListCommand;
import jfdi.storage.data.TaskAttributes;
import jfdi.storage.data.TaskDb;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * @author Liu Xinan
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TaskDb.class})
public class ListCommandTest {

    @Mock private Consumer<ListCommand> successHook;
    @Mock private Consumer<ListCommand> failureHook;

    private ArrayList<TaskAttributes> allItems = new ArrayList<>();
    private ArrayList<TaskAttributes> taggedItems = new ArrayList<>();
    private ArrayList<TaskAttributes> noItems = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        // Mocking TaskDb
        mockStatic(TaskDb.class);
        when(TaskDb.getAll()).thenReturn(allItems);
        when(TaskDb.getByTag("tag")).thenReturn(taggedItems);
        when(TaskDb.getByTag("non-exist")).thenReturn(noItems);

        allItems.add(mock(TaskAttributes.class));
        taggedItems.add(mock(TaskAttributes.class));
    }

    @Test
    public void testGetItems() throws Exception {
        // Command must be executed before there is any items to be gotten.
        ListCommand lsAll = new ListCommand.Builder().build();
        lsAll.execute();

        assertEquals(allItems, lsAll.getItems());
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
        ListCommand lsNonExist = new ListCommand.Builder().addTag("non-exist").build();
        assertNull(lsNonExist.getErrorType());

        lsNonExist.execute();
        assertEquals(lsNonExist.getErrorType(), ListCommand.ErrorType.NON_EXISTENT_TAG);
    }

    @Test
    public void testExecute() throws Exception {
        ListCommand lsWithoutTags = new ListCommand.Builder().build();
        lsWithoutTags.execute();

        verifyStatic();
        TaskDb.getAll();
        assertEquals(allItems, lsWithoutTags.getItems());

        ListCommand lsWithTags = new ListCommand.Builder().addTag("tag").build();
        lsWithTags.execute();

        verifyStatic();
        TaskDb.getByTag("tag");
        assertEquals(taggedItems, lsWithTags.getItems());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetItemsBeforeExecute() throws Exception {
        ListCommand command = new ListCommand.Builder().build();
        command.getItems();
    }

    @Test
    public void testAddSuccessHookAndOnSuccess() throws Exception {
        ListCommand.addSuccessHook(successHook);

        ListCommand ls = new ListCommand.Builder().build();
        ls.execute();

        verify(successHook).accept(ls);
    }

    @Test
    public void testAddFailureHookAndOnFailure() throws Exception {
        ListCommand.addFailureHook(failureHook);

        ListCommand ls = new ListCommand.Builder().addTag("non-exist").build();
        ls.execute();

        verify(failureHook).accept(ls);
    }

}
