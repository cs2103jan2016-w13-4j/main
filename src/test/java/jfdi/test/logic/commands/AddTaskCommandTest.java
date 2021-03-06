// @@author A0130195M

package jfdi.test.logic.commands;

import jfdi.logic.commands.AddTaskCommand;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.apis.TaskDb;
import jfdi.storage.exceptions.InvalidIdException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doThrow;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class AddTaskCommandTest extends CommonCommandTest {

    @Test
    public void testBuilder() throws Exception {
        LocalDateTime startDateTime = LocalDateTime.of(2016, 4, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2016, 4, 1, 23, 59);

        AddTaskCommand command = new AddTaskCommand.Builder()
            .setDescription("test")
            .setStartDateTime(startDateTime)
            .setEndDateTime(endDateTime)
            .build();

        assertEquals("test", command.getDescription());
        assertEquals(startDateTime, command.getStartDateTime().get());
        assertEquals(endDateTime, command.getEndDateTime().get());
    }

    @Test
    public void testExecute_successful() throws Exception {
        AddTaskCommand command = new AddTaskCommand.Builder()
            .setDescription("sleep")
            .build();

        command.execute();

        int id = command.getId();
        assertNotEquals(-1, id);

        TaskAttributes task = TaskDb.getInstance().getById(id);
        assertEquals("sleep", task.getDescription());

        TaskDb.getInstance().destroy(id);
    }

    @Test
    public void testExecute_emptyDescription() throws Exception {
        AddTaskCommand command = new AddTaskCommand.Builder().build();

        command.execute();

        assertEquals(-1, command.getId());
    }

    @Test
    public void testExecute_duplicatedTask() throws Exception {
        AddTaskCommand command1 = new AddTaskCommand.Builder()
            .setDescription("duplicate")
            .build();

        command1.execute();

        int id1 = command1.getId();
        assertNotEquals(-1, id1);

        AddTaskCommand command2 = new AddTaskCommand.Builder()
            .setDescription("duplicate")
            .build();

        command2.execute();

        assertEquals(-1, command2.getId());

        TaskDb.getInstance().destroy(id1);
    }

    @Test
    public void testUndo_successful() throws Exception {
        AddTaskCommand command = new AddTaskCommand.Builder()
            .setDescription("undo")
            .build();

        command.execute();

        int id = command.getId();

        Command.setTaskDb(TaskDb.getInstance());
        command.undo();
        Command.setTaskDb(taskDb);

        thrown.expect(InvalidIdException.class);
        TaskDb.getInstance().getById(id);
    }

    @Test
    public void testUndo_unsuccessful() throws Exception {
        doThrow(InvalidIdException.class).when(taskDb).destroy(anyInt());

        AddTaskCommand command = new AddTaskCommand.Builder()
            .setDescription("undo")
            .build();

        thrown.expect(AssertionError.class);
        command.undo();
    }

}
