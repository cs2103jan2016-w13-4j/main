package jfdi.test.logic.commands;

import jfdi.logic.commands.RenameTaskCommand;
import jfdi.logic.events.RenameTaskFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.apis.TaskDb;
import jfdi.storage.exceptions.InvalidIdException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class RenameTaskCommandTest extends CommonCommandTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();

        when(ui.getTaskId(1)).thenReturn(1);
        when(ui.getTaskId(2)).thenReturn(2);
        when(ui.getTaskId(3)).thenReturn(3);

        Command.setTaskDb(TaskDb.getInstance());
    }

    @After
    public void tearDown() throws Exception {
        Command.setTaskDb(taskDb);

        super.tearDown();
    }

    @Test
    public void testBuilder() throws Exception {
        RenameTaskCommand command = new RenameTaskCommand.Builder()
            .setId(1)
            .setDescription("Never gonna give")
            .build();

        assertEquals(1, command.getScreenId());
        assertEquals("Never gonna give", command.getDescription());
        assertNull(command.getOldDescription());
    }

    @Test
    public void testExecute_successful() throws Exception {
        TaskAttributes task = new TaskAttributes();
        task.setDescription("Task 1");
        task.save();

        RenameTaskCommand command = new RenameTaskCommand.Builder()
            .setId(task.getId())
            .setDescription("Never gonna give")
            .build();

        command.execute();

        assertEquals("Task 1", command.getOldDescription());
        assertNotEquals("Never gonna give", task.getDescription());

        TaskDb.getInstance().destroy(task.getId());
    }

    @Test
    public void testExecute_invalidId_unsuccessful() throws Exception {
        doThrow(InvalidIdException.class).when(taskDb).getById(anyInt());

        Command.setTaskDb(taskDb);

        RenameTaskCommand command = new RenameTaskCommand.Builder()
            .setId(1)
            .setDescription("Never gonna give")
            .build();

        command.execute();
        verify(eventBus).post(any(RenameTaskFailedEvent.class));
    }

    @Test
    public void testExecute_noAttributesChanged_unsuccessful() throws Exception {
        TaskAttributes task = new TaskAttributes();
        task.setDescription("Task");
        task.save();

        RenameTaskCommand command = new RenameTaskCommand.Builder()
            .setId(task.getId())
            .setDescription("Task")
            .build();

        command.execute();

        verify(eventBus).post(any(RenameTaskFailedEvent.class));

        TaskDb.getInstance().destroy(task.getId());
    }

    @Test
    public void testExecute_duplicateTask_unsuccessful() throws Exception {
        TaskAttributes task1 = new TaskAttributes();
        task1.setDescription("Task 1");
        task1.save();

        TaskAttributes task2 = new TaskAttributes();
        task2.setDescription("Task 2");
        task2.save();

        RenameTaskCommand command = new RenameTaskCommand.Builder()
            .setId(task2.getId())
            .setDescription("Task 1")
            .build();

        command.execute();

        verify(eventBus).post(any(RenameTaskFailedEvent.class));

        TaskDb.getInstance().destroy(task1.getId());
        TaskDb.getInstance().destroy(task2.getId());
    }

    @Test
    public void testExecute_invalidTaskParameter_unsuccessful() throws Exception {
        Command.setTaskDb(taskDb);

        TaskAttributes task = new TaskAttributes();
        task.setDescription("Task");
        task.save();

        when(taskDb.getById(anyInt())).thenReturn(task);
        TaskDb.getInstance().destroy(task.getId());

        RenameTaskCommand command = new RenameTaskCommand.Builder()
            .setId(task.getId())
            .setDescription("")
            .build();


        thrown.expect(AssertionError.class);
        command.execute();
    }

    @Test
    public void testUndo_successful() throws Exception {
        TaskAttributes task = new TaskAttributes();
        task.setDescription("Task 1");
        task.save();

        RenameTaskCommand command = new RenameTaskCommand.Builder()
            .setId(task.getId())
            .setDescription("Never gonna give")
            .build();

        command.execute();

        task = TaskDb.getInstance().getById(task.getId());
        assertEquals("Never gonna give", task.getDescription());

        command.undo();

        task = TaskDb.getInstance().getById(task.getId());
        assertEquals("Task 1", task.getDescription());

        TaskDb.getInstance().destroy(task.getId());
    }

    @Test
    public void testUndo_unsuccessful() throws Exception {
        Command.setTaskDb(taskDb);
        doThrow(InvalidIdException.class).when(taskDb).getById(anyInt());

        RenameTaskCommand command = new RenameTaskCommand.Builder()
            .setId(666)
            .setDescription("Never gonna give")
            .build();

        thrown.expect(AssertionError.class);
        command.undo();
    }

}
