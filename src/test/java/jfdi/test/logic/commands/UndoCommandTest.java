package jfdi.test.logic.commands;

import jfdi.logic.commands.AliasCommand;
import jfdi.logic.commands.UndoCommand;
import jfdi.logic.events.UndoFailedEvent;
import jfdi.logic.interfaces.Command;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class UndoCommandTest extends CommonCommandTest {

    @Test
    public void testExecute_successful() throws Exception {
        AliasCommand aliasCommand = mock(AliasCommand.class);
        doCallRealMethod().when(aliasCommand).pushToUndoStack();

        aliasCommand.pushToUndoStack();

        UndoCommand undoCommand = new UndoCommand.Builder().build();

        undoCommand.execute();

        verify(aliasCommand).undo();
    }

    @Test
    public void testExecute_unsuccessful() throws Exception {
        Command.getUndoStack().clear();

        UndoCommand undoCommand = new UndoCommand.Builder().build();

        undoCommand.execute();

        verify(eventBus).post(any(UndoFailedEvent.class));
    }

    @Test
    public void undo() throws Exception {
        UndoCommand command = new UndoCommand.Builder().build();

        thrown.expect(AssertionError.class);
        command.undo();
    }

}
