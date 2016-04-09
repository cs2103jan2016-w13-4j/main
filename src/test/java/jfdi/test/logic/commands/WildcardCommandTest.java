package jfdi.test.logic.commands;

import jfdi.logic.commands.WildcardCommand;
import jfdi.logic.events.NoSurpriseEvent;
import jfdi.logic.events.SurpriseEvent;
import jfdi.storage.apis.TaskAttributes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class WildcardCommandTest extends CommonCommandTest {

    @Test
    public void testExecute_successful() throws Exception {
        TaskAttributes task = new TaskAttributes();
        task.setDescription("Never gonna give you up");
        task.setCompleted(false);

        ArrayList<TaskAttributes> tasks = new ArrayList<>();
        tasks.add(task);

        when(taskDb.getAll()).thenReturn(tasks);

        WildcardCommand command = new WildcardCommand.Builder().build();

        assertNull(command.getLucky());

        command.execute();

        assertEquals(task, command.getLucky());
        verify(eventBus).post(any(SurpriseEvent.class));
    }

    @Test
    public void testExecute_unsuccessful() throws Exception {
        when(taskDb.getAll()).thenReturn(new ArrayList<>());

        WildcardCommand command = new WildcardCommand.Builder().build();

        assertNull(command.getLucky());

        command.execute();

        assertNull(command.getLucky());
        verify(eventBus).post(any(NoSurpriseEvent.class));
    }

    @Test
    public void undo() throws Exception {
        WildcardCommand command = new WildcardCommand.Builder().build();

        thrown.expect(AssertionError.class);
        command.undo();
    }

}
