// @@author A0130195M

package jfdi.test.logic.commands;

import jfdi.logic.commands.ListCommand;
import jfdi.logic.events.ListDoneEvent;
import jfdi.storage.apis.TaskAttributes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class ListCommandTest extends CommonCommandTest {

    private ArgumentCaptor<ListDoneEvent> argument = ArgumentCaptor.forClass(ListDoneEvent.class);

    @Before
    public void setUp() throws Exception {
        super.setUp();

        TaskAttributes floating = new TaskAttributes();

        TaskAttributes upcoming = new TaskAttributes();
        upcoming.setStartDateTime(LocalDateTime.now().plusDays(1));

        TaskAttributes overdue = new TaskAttributes();
        overdue.setStartDateTime(LocalDateTime.now().minusDays(1));

        TaskAttributes incomplete = new TaskAttributes();
        incomplete.setCompleted(false);

        TaskAttributes completed = new TaskAttributes();
        completed.setCompleted(true);

        ArrayList<TaskAttributes> tasks = new ArrayList<>();
        tasks.add(floating);
        tasks.add(upcoming);
        tasks.add(overdue);
        tasks.add(incomplete);
        tasks.add(completed);

        ArrayList<TaskAttributes> overdueTasks = new ArrayList<>();
        overdueTasks.add(overdue);

        ArrayList<TaskAttributes> upcomingTasks = new ArrayList<>();
        upcomingTasks.add(upcoming);

        when(taskDb.getAll()).thenReturn(tasks);
        when(taskDb.getOverdue()).thenReturn(overdueTasks);
        when(taskDb.getUpcoming()).thenReturn(upcomingTasks);
    }

    @Test
    public void testBuilder() throws Exception {
        ListCommand listAll = new ListCommand.Builder()
            .setListType(ListCommand.ListType.ALL)
            .build();

        assertEquals(ListCommand.ListType.ALL, listAll.getListType());

        ListCommand listIncomplete = new ListCommand.Builder()
            .setListType(ListCommand.ListType.INCOMPLETE)
            .build();

        assertEquals(ListCommand.ListType.INCOMPLETE, listIncomplete.getListType());

        ListCommand listUpcoming = new ListCommand.Builder()
            .setListType(ListCommand.ListType.UPCOMING)
            .build();

        assertEquals(ListCommand.ListType.UPCOMING, listUpcoming.getListType());

        ListCommand listOverdue = new ListCommand.Builder()
            .setListType(ListCommand.ListType.OVERDUE)
            .build();

        assertEquals(ListCommand.ListType.OVERDUE, listOverdue.getListType());

        ListCommand listCompleted = new ListCommand.Builder()
            .setListType(ListCommand.ListType.COMPLETED)
            .build();

        assertEquals(ListCommand.ListType.COMPLETED, listCompleted.getListType());
    }

    @Test
    public void testExecute_listAll() throws Exception {
        ListCommand listAll = new ListCommand.Builder()
            .setListType(ListCommand.ListType.ALL)
            .build();

        listAll.execute();

        verify(eventBus).post(argument.capture());
        assertEquals(5, argument.getValue().getItems().size());
    }

    @Test
    public void testExecute_listIncomplete() throws Exception {
        ListCommand listIncomplete = new ListCommand.Builder()
            .setListType(ListCommand.ListType.INCOMPLETE)
            .build();

        listIncomplete.execute();

        verify(eventBus).post(argument.capture());
        assertEquals(4, argument.getValue().getItems().size());

        for (int i = 0; i < 4; i++) {
            assertFalse(argument.getValue().getItems().get(i).isCompleted());
        }
    }

    @Test
    public void testExecute_listUpcoming() throws Exception {
        ListCommand listUpcoming = new ListCommand.Builder()
            .setListType(ListCommand.ListType.UPCOMING)
            .build();

        listUpcoming.execute();

        verify(eventBus).post(argument.capture());
        assertEquals(1, argument.getValue().getItems().size());
        assertTrue(argument.getValue().getItems().get(0).getStartDateTime().isAfter(LocalDateTime.now()));
    }

    @Test
    public void testExecute_listOverdue() throws Exception {
        ListCommand listOverdue = new ListCommand.Builder()
            .setListType(ListCommand.ListType.OVERDUE)
            .build();

        listOverdue.execute();

        verify(eventBus).post(argument.capture());
        assertEquals(1, argument.getValue().getItems().size());
        assertTrue(argument.getValue().getItems().get(0).getStartDateTime().isBefore(LocalDateTime.now()));
    }

    @Test
    public void testExecute_listCompleted() throws Exception {
        ListCommand listCompleted = new ListCommand.Builder()
            .setListType(ListCommand.ListType.COMPLETED)
            .build();

        listCompleted.execute();

        verify(eventBus).post(argument.capture());
        assertEquals(1, argument.getValue().getItems().size());
        assertTrue(argument.getValue().getItems().get(0).isCompleted());
    }

    @Test
    public void undo() throws Exception {
        ListCommand listAll = new ListCommand.Builder()
            .setListType(ListCommand.ListType.ALL)
            .build();

        thrown.expect(AssertionError.class);
        listAll.undo();
    }

}
