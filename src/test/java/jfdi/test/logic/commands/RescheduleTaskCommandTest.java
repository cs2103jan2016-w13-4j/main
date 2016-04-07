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
    public void testExecute_nonShifting_successful() throws Exception {
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
    public void testExecute_shiftingFloatingTask_successful() throws Exception {
        TaskAttributes floating = mock(TaskAttributes.class);
        when(floating.getStartDateTime()).thenReturn(null);
        when(floating.getEndDateTime()).thenReturn(null);
        when(ui.getTaskId(1)).thenReturn(1);
        when(taskDb.getById(1)).thenReturn(floating);

        LocalDateTime shiftedDateTime = LocalDateTime.now().plusDays(1);

        RescheduleTaskCommand command = new RescheduleTaskCommand.Builder()
            .setId(1)
            .setShiftedDateTime(shiftedDateTime)
            .setShiftedDateSpecified(true)
            .setShiftedTimeSpecified(true)
            .build();

        command.execute();

        verify(floating).setStartDateTime(shiftedDateTime);
        verify(floating).setEndDateTime(null);
        verify(floating).save();
    }

    @Test
    public void testExecute_shiftingPointTask_successful() throws Exception {
        TaskAttributes point = mock(TaskAttributes.class);
        when(point.getStartDateTime()).thenReturn(LocalDateTime.now());
        when(point.getEndDateTime()).thenReturn(null);
        when(ui.getTaskId(2)).thenReturn(2);
        when(taskDb.getById(2)).thenReturn(point);

        LocalDateTime shiftedDateTime = LocalDateTime.now().plusDays(1);

        RescheduleTaskCommand command = new RescheduleTaskCommand.Builder()
            .setId(2)
            .setShiftedDateTime(shiftedDateTime)
            .setShiftedDateSpecified(true)
            .setShiftedTimeSpecified(true)
            .build();

        command.execute();

        verify(point).setStartDateTime(shiftedDateTime);
        verify(point).setEndDateTime(null);
        verify(point).save();
    }

    @Test
    public void testExecute_shiftingDeadlineTask_successful() throws Exception {
        TaskAttributes deadline = mock(TaskAttributes.class);
        when(deadline.getStartDateTime()).thenReturn(null);
        when(deadline.getEndDateTime()).thenReturn(LocalDateTime.now());
        when(ui.getTaskId(3)).thenReturn(3);
        when(taskDb.getById(3)).thenReturn(deadline);

        LocalDateTime shiftedDateTime = LocalDateTime.now().plusDays(1);

        RescheduleTaskCommand command = new RescheduleTaskCommand.Builder()
            .setId(3)
            .setShiftedDateTime(shiftedDateTime)
            .setShiftedDateSpecified(true)
            .setShiftedTimeSpecified(true)
            .build();

        command.execute();

        verify(deadline).setStartDateTime(null);
        verify(deadline).setEndDateTime(shiftedDateTime);
        verify(deadline).save();
    }

    @Test
    public void testExecute_shiftingEventTask_bothDateTimeSpecified_successful() throws Exception {
        TaskAttributes event = mock(TaskAttributes.class);
        when(event.getStartDateTime()).thenReturn(LocalDateTime.now());
        when(event.getEndDateTime()).thenReturn(LocalDateTime.now().plusHours(1));
        when(ui.getTaskId(4)).thenReturn(4);
        when(taskDb.getById(4)).thenReturn(event);

        LocalDateTime shiftedDateTime = LocalDateTime.now().plusDays(1);

        RescheduleTaskCommand command = new RescheduleTaskCommand.Builder()
            .setId(4)
            .setShiftedDateTime(shiftedDateTime)
            .setShiftedDateSpecified(true)
            .setShiftedTimeSpecified(true)
            .build();

        command.execute();

        verify(event).setStartDateTime(shiftedDateTime);
        verify(event).setEndDateTime(shiftedDateTime.plusHours(1));
        verify(event).save();
    }

    @Test
    public void testExecute_shiftingEventTask_onlyDateSpecified_successful() throws Exception {
        LocalDateTime oldStartDateTime = LocalDateTime.now();
        LocalDateTime oldEndDateTime = oldStartDateTime.plusHours(1);

        TaskAttributes event = mock(TaskAttributes.class);
        when(event.getStartDateTime()).thenReturn(oldStartDateTime);
        when(event.getEndDateTime()).thenReturn(oldEndDateTime);
        when(ui.getTaskId(4)).thenReturn(4);
        when(taskDb.getById(4)).thenReturn(event);

        LocalDateTime shiftedDateTime = LocalDateTime.now().plusDays(1);

        RescheduleTaskCommand command = new RescheduleTaskCommand.Builder()
            .setId(4)
            .setShiftedDateTime(shiftedDateTime)
            .setShiftedDateSpecified(true)
            .setShiftedTimeSpecified(false)
            .build();

        command.execute();

        verify(event).setStartDateTime(oldStartDateTime.plusDays(1));
        verify(event).setEndDateTime(oldEndDateTime.plusDays(1));
        verify(event).save();
    }

    @Test
    public void testExecute_shiftingEventTask_onlyTimeSpecified_successful() throws Exception {
        LocalDateTime oldStartDateTime = LocalDateTime.now();
        LocalDateTime oldEndDateTime = oldStartDateTime.plusHours(1);

        TaskAttributes event = mock(TaskAttributes.class);
        when(event.getStartDateTime()).thenReturn(oldStartDateTime);
        when(event.getEndDateTime()).thenReturn(oldEndDateTime);
        when(ui.getTaskId(4)).thenReturn(4);
        when(taskDb.getById(4)).thenReturn(event);

        LocalDateTime shiftedDateTime = oldStartDateTime.plusDays(1);

        RescheduleTaskCommand command = new RescheduleTaskCommand.Builder()
            .setId(4)
            .setShiftedDateTime(shiftedDateTime)
            .setShiftedDateSpecified(false)
            .setShiftedTimeSpecified(true)
            .build();

        command.execute();

        verify(event).setStartDateTime(oldStartDateTime);
        verify(event).setEndDateTime(oldEndDateTime);
        verify(event).save();
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
