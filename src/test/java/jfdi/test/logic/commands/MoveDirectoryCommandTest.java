// @@author A0130195M

package jfdi.test.logic.commands;

import jfdi.logic.commands.MoveDirectoryCommand;
import jfdi.logic.events.MoveDirectoryFailedEvent;
import jfdi.storage.exceptions.FilesReplacedException;
import jfdi.storage.exceptions.InvalidFilePathException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class MoveDirectoryCommandTest extends CommonCommandTest {

    @Test
    public void testBuilder() throws Exception {
        MoveDirectoryCommand command = new MoveDirectoryCommand.Builder()
            .setNewDirectory("./new")
            .build();

        assertEquals("./new", command.getNewDirectory());
        assertNull(command.getOldDirectory());
    }

    @Test
    public void testExecute_successful() throws Exception {
        when(mainStorage.getCurrentDirectory()).thenReturn("./old");

        MoveDirectoryCommand command = new MoveDirectoryCommand.Builder()
            .setNewDirectory("./new")
            .build();

        command.execute();

        verify(mainStorage).changeDirectory("./new");
        assertEquals("./old", command.getOldDirectory());
    }

    @Test
    public void testExecute_filesReplaced_successful() throws Exception {
        when(mainStorage.getCurrentDirectory()).thenReturn("./old");
        doThrow(FilesReplacedException.class).when(mainStorage).changeDirectory("./new");

        MoveDirectoryCommand command = new MoveDirectoryCommand.Builder()
            .setNewDirectory("./new")
            .build();

        command.execute();

        verify(eventBus, times(2)).post(any());
        assertEquals("./old", command.getOldDirectory());
    }

    @Test
    public void testExecute_invalidPath_successful() throws Exception {
        when(mainStorage.getCurrentDirectory()).thenReturn("./old");
        doThrow(InvalidFilePathException.class).when(mainStorage).changeDirectory("./new");

        MoveDirectoryCommand command = new MoveDirectoryCommand.Builder()
            .setNewDirectory("./new")
            .build();

        command.execute();

        verify(eventBus).post(any(MoveDirectoryFailedEvent.class));
        assertEquals("./old", command.getOldDirectory());
    }

    @Test
    public void testUndo_successful() throws Exception {
        when(mainStorage.getCurrentDirectory()).thenReturn("./old");

        MoveDirectoryCommand command = new MoveDirectoryCommand.Builder()
            .setNewDirectory("./new")
            .build();

        command.execute();
        command.undo();

        verify(mainStorage).changeDirectory("./old");
    }

    @Test
    public void testUndo_filesReplaced_unsuccessful() throws Exception {
        when(mainStorage.getCurrentDirectory()).thenReturn("./old");
        doThrow(FilesReplacedException.class).when(mainStorage).changeDirectory("./old");

        MoveDirectoryCommand command = new MoveDirectoryCommand.Builder()
            .setNewDirectory("./new")
            .build();

        command.execute();
        command.undo();

        verify(mainStorage).changeDirectory("./old");
        verify(eventBus, times(2)).post(any());
    }

    @Test
    public void testUndo_invalidPath_unsuccessful() throws Exception {
        when(mainStorage.getCurrentDirectory()).thenReturn("./old");
        doThrow(InvalidFilePathException.class).when(mainStorage).changeDirectory("./old");

        MoveDirectoryCommand command = new MoveDirectoryCommand.Builder()
            .setNewDirectory("./new")
            .build();

        command.execute();

        thrown.expect(AssertionError.class);
        command.undo();

        verify(mainStorage).changeDirectory("./old");
    }

}
