package jfdi.test.logic.commands;

import jfdi.logic.commands.MarkTaskCommand;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.NoAttributesChangedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class MarkTaskCommandTest extends CommonCommandTest {

    @Test
    public void testBuilder() throws Exception {
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(2);
        ids.add(3);

        MarkTaskCommand command = new MarkTaskCommand.Builder()
            .addTaskId(1)
            .addTaskIds(ids)
            .build();

        assertEquals(3, command.getScreenIds().size());

        assertTrue(command.getScreenIds().contains(1));
        assertTrue(command.getScreenIds().contains(2));
        assertTrue(command.getScreenIds().contains(3));
        assertNull(command.getMarkedIds());
    }

    @Test
    public void testExecute_markSingle_successful() throws Exception {
        when(ui.getTaskId(1)).thenReturn(1);
        when(taskDb.hasId(1)).thenReturn(true);
        when(taskDb.getById(anyInt())).thenReturn(new TaskAttributes());

        MarkTaskCommand command = new MarkTaskCommand.Builder()
            .addTaskId(1)
            .build();

        command.execute();

        assertEquals(1, command.getMarkedIds().size());
    }

    @Test
    public void testExecute_markSingle_alreadyCompleted_successful() throws Exception {
        when(ui.getTaskId(1)).thenReturn(1);
        when(taskDb.hasId(1)).thenReturn(true);
        when(taskDb.getById(anyInt())).thenReturn(new TaskAttributes());
        doThrow(NoAttributesChangedException.class).when(taskDb).markAsComplete(anyInt());

        MarkTaskCommand command = new MarkTaskCommand.Builder()
            .addTaskId(1)
            .build();

        command.execute();
        assertEquals(0, command.getMarkedIds().size());
    }

    @Test
    public void testExecute_markMultiple_successful() throws Exception {
        TaskAttributes task1 = new TaskAttributes();
        TaskAttributes task2 = new TaskAttributes();
        TaskAttributes task3 = new TaskAttributes();
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);

        when(ui.getTaskId(1)).thenReturn(1);
        when(ui.getTaskId(2)).thenReturn(2);
        when(ui.getTaskId(3)).thenReturn(3);
        when(taskDb.hasId(1)).thenReturn(true);
        when(taskDb.hasId(2)).thenReturn(true);
        when(taskDb.hasId(3)).thenReturn(true);
        when(taskDb.getById(1)).thenReturn(task1);
        when(taskDb.getById(2)).thenReturn(task2);
        when(taskDb.getById(3)).thenReturn(task3);

        MarkTaskCommand command = new MarkTaskCommand.Builder()
            .addTaskId(1)
            .addTaskId(2)
            .addTaskId(3)
            .build();

        command.execute();

        assertEquals(3, command.getMarkedIds().size());
    }

    @Test
    public void testExecute_shouldNotHappen_unsuccessful() throws Exception {
        when(ui.getTaskId(1)).thenReturn(1);
        when(taskDb.hasId(1)).thenReturn(true);
        when(taskDb.getById(anyInt())).thenReturn(new TaskAttributes());
        doThrow(InvalidIdException.class).when(taskDb).markAsComplete(anyInt());

        MarkTaskCommand command = new MarkTaskCommand.Builder()
            .addTaskId(1)
            .build();

        thrown.expect(AssertionError.class);
        command.execute();
    }

    @Test
    public void testExecute_unsuccessful() throws Exception {
        TaskAttributes task1 = new TaskAttributes();
        TaskAttributes task3 = new TaskAttributes();
        task1.setId(1);
        task3.setId(3);

        when(ui.getTaskId(1)).thenReturn(1);
        when(ui.getTaskId(2)).thenReturn(2);
        when(ui.getTaskId(3)).thenReturn(3);
        when(taskDb.hasId(1)).thenReturn(true);
        when(taskDb.hasId(2)).thenReturn(false);
        when(taskDb.hasId(3)).thenReturn(true);
        when(taskDb.getById(1)).thenReturn(task1);
        when(taskDb.getById(3)).thenReturn(task3);

        MarkTaskCommand command = new MarkTaskCommand.Builder()
            .addTaskId(1)
            .addTaskId(2)
            .addTaskId(3)
            .build();

        command.execute();

        assertNull(command.getMarkedIds());
    }

    @Test
    public void testUndo_successful() throws Exception {
        TaskAttributes task1 = new TaskAttributes();
        TaskAttributes task2 = new TaskAttributes();
        TaskAttributes task3 = new TaskAttributes();
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);

        when(ui.getTaskId(1)).thenReturn(1);
        when(ui.getTaskId(2)).thenReturn(2);
        when(ui.getTaskId(3)).thenReturn(3);
        when(taskDb.hasId(1)).thenReturn(true);
        when(taskDb.hasId(2)).thenReturn(true);
        when(taskDb.hasId(3)).thenReturn(true);
        when(taskDb.getById(1)).thenReturn(task1);
        when(taskDb.getById(2)).thenReturn(task2);
        when(taskDb.getById(3)).thenReturn(task3);

        MarkTaskCommand command = new MarkTaskCommand.Builder()
            .addTaskId(1)
            .addTaskId(2)
            .addTaskId(3)
            .build();

        command.execute();
        command.undo();

        verify(taskDb).markAsIncomplete(1);
        verify(taskDb).markAsIncomplete(2);
        verify(taskDb).markAsIncomplete(3);
    }

    @Test
    public void testUndo_unsuccessful() throws Exception {
        when(ui.getTaskId(1)).thenReturn(1);
        when(taskDb.hasId(1)).thenReturn(true);
        when(taskDb.getById(anyInt())).thenReturn(new TaskAttributes());
        doThrow(InvalidIdException.class).when(taskDb).markAsIncomplete(anyInt());

        MarkTaskCommand command = new MarkTaskCommand.Builder()
            .addTaskId(1)
            .build();

        command.execute();

        thrown.expect(AssertionError.class);
        command.undo();
    }

}
