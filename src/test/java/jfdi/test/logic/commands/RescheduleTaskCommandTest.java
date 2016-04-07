package jfdi.test.logic.commands;

import jfdi.logic.commands.RescheduleTaskCommand;
import jfdi.logic.events.RescheduleTaskFailedEvent;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.exceptions.DuplicateTaskException;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.InvalidTaskParametersException;
import jfdi.storage.exceptions.NoAttributesChangedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class RescheduleTaskCommandTest extends CommonCommandTest {

    @Test
    public void testBuilder() throws Exception {
        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(2);

        RescheduleTaskCommand command = new RescheduleTaskCommand.Builder()
            .setId(1)
            .setStartDateTime(startDateTime)
            .setEndDateTime(endDateTime)
            .setShiftedDateSpecified(true)
            .setShiftedTimeSpecified(true)
            .build();

        assertEquals(1, command.getScreenId());
        assertEquals(startDateTime, command.getStartDateTime());
        assertEquals(endDateTime, command.getEndDateTime());
        assertNull(command.getOldStartDateTime());
        assertNull(command.getOldEndDateTime());
        assertTrue(command.isShiftedDateSpecified());
        assertTrue(command.isShiftedTimeSpecified());
    }

    @Test
    public void testExecute_successful() throws Exception {
        TaskAttributes task = mock(TaskAttributes.class);
        when(ui.getTaskId(1)).thenReturn(1);
        when(taskDb.getById(1)).thenReturn(task);

        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(2);

        RescheduleTaskCommand command = new RescheduleTaskCommand.Builder()
            .setId(1)
            .setStartDateTime(startDateTime)
            .setEndDateTime(endDateTime)
            .setShiftedDateSpecified(true)
            .setShiftedTimeSpecified(true)
            .build();

        command.execute();

        verify(task).setStartDateTime(any(LocalDateTime.class));
        verify(task).setEndDateTime(any(LocalDateTime.class));
        verify(task).save();
    }

    @Test
    public void testExecute_invalidId_unsuccessful() throws Exception {
        when(ui.getTaskId(1)).thenReturn(1);
        doThrow(InvalidIdException.class).when(taskDb).getById(1);

        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(2);

        RescheduleTaskCommand command = new RescheduleTaskCommand.Builder()
            .setId(1)
            .setStartDateTime(startDateTime)
            .setEndDateTime(endDateTime)
            .setShiftedDateSpecified(true)
            .setShiftedTimeSpecified(true)
            .build();

        command.execute();

        verify(eventBus).post(any(RescheduleTaskFailedEvent.class));
    }

    @Test
    public void testExecute_invalidTaskParameter_unsuccessful() throws Exception {
        TaskAttributes task = mock(TaskAttributes.class);
        when(ui.getTaskId(1)).thenReturn(1);
        when(taskDb.getById(1)).thenReturn(task);
        doThrow(InvalidTaskParametersException.class).when(task).save();

        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(2);

        RescheduleTaskCommand command = new RescheduleTaskCommand.Builder()
            .setId(1)
            .setStartDateTime(startDateTime)
            .setEndDateTime(endDateTime)
            .setShiftedDateSpecified(true)
            .setShiftedTimeSpecified(true)
            .build();

        thrown.expect(AssertionError.class);
        command.execute();
    }

    @Test
    public void testExecute_noAttributesChanged_unsuccessful() throws Exception {
        TaskAttributes task = mock(TaskAttributes.class);
        when(ui.getTaskId(1)).thenReturn(1);
        when(taskDb.getById(1)).thenReturn(task);
        doThrow(NoAttributesChangedException.class).when(task).save();

        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(2);

        RescheduleTaskCommand command = new RescheduleTaskCommand.Builder()
            .setId(1)
            .setStartDateTime(startDateTime)
            .setEndDateTime(endDateTime)
            .setShiftedDateSpecified(true)
            .setShiftedTimeSpecified(true)
            .build();

        command.execute();

        verify(task).setStartDateTime(any(LocalDateTime.class));
        verify(task).setEndDateTime(any(LocalDateTime.class));
        verify(task).save();
        verify(eventBus).post(any(RescheduleTaskFailedEvent.class));
    }

    @Test
    public void testExecute_duplicateTask_unsuccessful() throws Exception {
        TaskAttributes task = mock(TaskAttributes.class);
        when(ui.getTaskId(1)).thenReturn(1);
        when(taskDb.getById(1)).thenReturn(task);
        doThrow(DuplicateTaskException.class).when(task).save();

        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(2);

        RescheduleTaskCommand command = new RescheduleTaskCommand.Builder()
            .setId(1)
            .setStartDateTime(startDateTime)
            .setEndDateTime(endDateTime)
            .setShiftedDateSpecified(true)
            .setShiftedTimeSpecified(true)
            .build();

        command.execute();

        verify(task).setStartDateTime(any(LocalDateTime.class));
        verify(task).setEndDateTime(any(LocalDateTime.class));
        verify(task).save();
        verify(eventBus).post(any(RescheduleTaskFailedEvent.class));
    }

    @Test
    public void testUndo_successful() throws Exception {
        TaskAttributes task = mock(TaskAttributes.class);
        when(ui.getTaskId(1)).thenReturn(1);
        when(taskDb.getById(1)).thenReturn(task);

        LocalDateTime oldStartDateTime = LocalDateTime.now().minusDays(2);
        LocalDateTime oldEndDateTime = LocalDateTime.now().minusDays(2);
        when(task.getStartDateTime()).thenReturn(oldStartDateTime);
        when(task.getEndDateTime()).thenReturn(oldEndDateTime);

        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(2);

        RescheduleTaskCommand command = new RescheduleTaskCommand.Builder()
            .setId(1)
            .setStartDateTime(startDateTime)
            .setEndDateTime(endDateTime)
            .setShiftedDateSpecified(true)
            .setShiftedTimeSpecified(true)
            .build();

        command.execute();

        assertEquals(oldStartDateTime, command.getOldStartDateTime());
        assertEquals(oldEndDateTime, command.getOldEndDateTime());
        verify(task).setStartDateTime(startDateTime);
        verify(task).setEndDateTime(endDateTime);
        verify(task).save();

        command.undo();

        verify(task).setStartDateTime(oldStartDateTime);
        verify(task).setEndDateTime(oldEndDateTime);
    }

    @Test
    public void testUndo_unsuccessful() throws Exception {
        TaskAttributes task = mock(TaskAttributes.class);
        when(ui.getTaskId(1)).thenReturn(1);
        when(taskDb.getById(1)).thenReturn(task);
        doThrow(InvalidTaskParametersException.class).when(task).save();

        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(2);

        RescheduleTaskCommand command = new RescheduleTaskCommand.Builder()
            .setId(1)
            .setStartDateTime(startDateTime)
            .setEndDateTime(endDateTime)
            .setShiftedDateSpecified(true)
            .setShiftedTimeSpecified(true)
            .build();

        thrown.expect(AssertionError.class);
        command.undo();
    }

}
