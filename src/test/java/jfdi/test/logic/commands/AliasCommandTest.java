// @@author A0130195M

package jfdi.test.logic.commands;

import jfdi.logic.commands.AliasCommand;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.AliasAttributes;
import jfdi.storage.apis.AliasDb;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.verify;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class AliasCommandTest extends CommonCommandTest {

    @Test
    public void testBuilder() throws Exception {
        AliasCommand command1 = new AliasCommand.Builder()
            .setCommand("list")
            .setAlias("ls")
            .setIsValid(true)
            .build();

        assertEquals("list", command1.getCommand());
        assertEquals("ls", command1.getAlias());
        assertTrue(command1.isValid());

        AliasCommand command2 = new AliasCommand.Builder()
            .setCommand("ls")
            .setAlias("list")
            .setIsValid(false)
            .build();

        assertEquals("ls", command2.getCommand());
        assertEquals("list", command2.getAlias());
        assertFalse(command2.isValid());
    }

    @Test
    public void testExecute_successful() throws Exception {
        AliasCommand command = new AliasCommand.Builder()
            .setCommand("list")
            .setAlias("ls")
            .setIsValid(true)
            .build();

        command.execute();

        String commandString = AliasDb.getInstance().getCommandFromAlias("ls");
        assertEquals("list", commandString);

        AliasDb.getInstance().destroy("ls");
    }

    @Test
    public void testExecute_duplicateAlias() throws Exception {
        AliasCommand command1 = new AliasCommand.Builder()
            .setCommand("list")
            .setAlias("ls")
            .setIsValid(true)
            .build();

        command1.execute();

        int currentUndoStackSize = Command.getUndoStack().size();

        AliasCommand command2 = new AliasCommand.Builder()
            .setCommand("list")
            .setAlias("ls")
            .setIsValid(true)
            .build();

        command2.execute();

        assertEquals(currentUndoStackSize, Command.getUndoStack().size());

        String commandString = AliasDb.getInstance().getCommandFromAlias("ls");
        assertEquals("list", commandString);

        AliasDb.getInstance().destroy("ls");
    }

    @Test
    public void testExecute_invalidAlias() throws Exception {
        AliasCommand command = new AliasCommand.Builder()
            .setCommand("list")
            .setAlias("add")
            .setIsValid(true)
            .build();

        int currentUndoStackSize = Command.getUndoStack().size();

        command.execute();

        assertEquals(currentUndoStackSize, Command.getUndoStack().size());
    }

    @Test
    public void testUndo() throws Exception {
        AliasCommand command = new AliasCommand.Builder()
            .setCommand("list")
            .setAlias("ls")
            .setIsValid(true)
            .build();

        command.undo();
        verify(aliasDb).destroy("ls");
        verify(parser).setAliases(anyCollectionOf(AliasAttributes.class));
    }

}
