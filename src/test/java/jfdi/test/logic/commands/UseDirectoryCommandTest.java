// @@author A0130195M

package jfdi.test.logic.commands;

import jfdi.logic.commands.UseDirectoryCommand;
import jfdi.logic.events.UseDirectoryDoneEvent;
import jfdi.storage.exceptions.FilesReplacedException;
import jfdi.storage.exceptions.InvalidFilePathException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class UseDirectoryCommandTest extends CommonCommandTest {

    @Test
    public void getNewDirectory() throws Exception {
        UseDirectoryCommand command = new UseDirectoryCommand.Builder()
            .setNewDirectory("./new")
            .build();

        assertEquals("./new", command.getNewDirectory());
        assertNull(command.getOldDirectory());
    }

    @Test
    public void testExecute_successful() throws Exception {
        when(mainStorage.getCurrentDirectory()).thenReturn("./old");

        UseDirectoryCommand command = new UseDirectoryCommand.Builder()
            .setNewDirectory("./new")
            .build();

        command.execute();

        verify(mainStorage).use("./new");
        assertEquals("./old", command.getOldDirectory());
    }

    @Test
    public void testExecute_filesReplaced_successful() throws Exception {
        when(mainStorage.getCurrentDirectory()).thenReturn("./old");
        doThrow(FilesReplacedException.class).when(mainStorage).use("./new");

        UseDirectoryCommand command = new UseDirectoryCommand.Builder()
            .setNewDirectory("./new")
            .build();

        command.execute();

        verify(eventBus, times(2)).post(any());
        assertEquals("./old", command.getOldDirectory());
    }

    @Test
    public void testExecute_invalidPath_successful() throws Exception {
        when(mainStorage.getCurrentDirectory()).thenReturn("./old");
        doThrow(InvalidFilePathException.class).when(mainStorage).use("./new");

        UseDirectoryCommand command = new UseDirectoryCommand.Builder()
            .setNewDirectory("./new")
            .build();

        command.execute();

        verify(eventBus).post(any(UseDirectoryDoneEvent.class));
        assertEquals("./old", command.getOldDirectory());
    }

    @Test
    public void testUndo_successful() throws Exception {
        when(mainStorage.getCurrentDirectory()).thenReturn("./old");

        UseDirectoryCommand command = new UseDirectoryCommand.Builder()
            .setNewDirectory("./new")
            .build();

        command.execute();
        command.undo();

        verify(mainStorage).use("./old");
    }

    @Test
    public void testUndo_filesReplaced_unsuccessful() throws Exception {
        when(mainStorage.getCurrentDirectory()).thenReturn("./old");
        doThrow(FilesReplacedException.class).when(mainStorage).use("./old");

        UseDirectoryCommand command = new UseDirectoryCommand.Builder()
            .setNewDirectory("./new")
            .build();

        command.execute();
        command.undo();

        verify(mainStorage).use("./old");
        verify(eventBus, times(2)).post(any());
    }

    @Test
    public void testUndo_invalidPath_unsuccessful() throws Exception {
        when(mainStorage.getCurrentDirectory()).thenReturn("./old");
        doThrow(InvalidFilePathException.class).when(mainStorage).use("./old");

        UseDirectoryCommand command = new UseDirectoryCommand.Builder()
            .setNewDirectory("./new")
            .build();

        command.execute();

        thrown.expect(AssertionError.class);
        command.undo();

        verify(mainStorage).use("./old");
    }

}
