package jfdi.test.logic.commands;

import jfdi.logic.commands.UnaliasCommand;
import jfdi.logic.events.UnaliasFailedEvent;
import jfdi.storage.apis.AliasAttributes;
import jfdi.storage.exceptions.InvalidAliasException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.*;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class UnaliasCommandTest extends CommonCommandTest {

    @Test
    public void testBuilder() throws Exception {
        UnaliasCommand command = new UnaliasCommand.Builder()
            .setAlias("ls")
            .build();

        assertEquals("ls", command.getAlias());
    }

    @Test
    public void testExecute_successful() throws Exception {
        UnaliasCommand command = new UnaliasCommand.Builder()
            .setAlias("ls")
            .build();

        command.execute();

        verify(aliasDb).destroy("ls");
        verify(aliasDb).getAll();
        verify(parser).setAliases(anyCollectionOf(AliasAttributes.class));
    }

    @Test
    public void testExecute_unsuccessful() throws Exception {
        doThrow(InvalidAliasException.class).when(aliasDb).destroy("ls");

        UnaliasCommand command = new UnaliasCommand.Builder()
            .setAlias("ls")
            .build();

        command.execute();

        verify(aliasDb).destroy("ls");
        verifyNoMoreInteractions(aliasDb);
        verifyZeroInteractions(parser);
        verify(eventBus).post(any(UnaliasFailedEvent.class));
    }

    @Test
    public void testUndo_successful() throws Exception {
        UnaliasCommand command = new UnaliasCommand.Builder()
            .setAlias("ls")
            .build();

        command.undo();

        verify(aliasDb).undestroy("ls");
        verify(aliasDb).getAll();
        verify(parser).setAliases(anyCollectionOf(AliasAttributes.class));
    }

    @Test
    public void testUndo_unsuccessful() throws Exception {
        doThrow(InvalidAliasException.class).when(aliasDb).undestroy("ls");

        UnaliasCommand command = new UnaliasCommand.Builder()
            .setAlias("ls")
            .build();

        thrown.expect(AssertionError.class);
        command.undo();
    }

}
