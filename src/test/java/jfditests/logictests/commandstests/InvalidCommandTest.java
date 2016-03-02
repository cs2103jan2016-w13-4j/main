package jfditests.logictests.commandstests;

import jfdi.logic.commands.InvalidCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class InvalidCommandTest {

    @Mock private Consumer<InvalidCommand> successHook;
    @Mock private Consumer<InvalidCommand> failureHook;

    private InvalidCommand invalidCommand = new InvalidCommand.Builder().build();

    @Test
    public void testInvalidCommand() throws Exception {
        InvalidCommand.addSuccessHook(successHook);
        InvalidCommand.addFailureHook(failureHook);

        invalidCommand.execute();

        verify(failureHook).accept(invalidCommand);
        // Invalid command always fail.
        verifyNoMoreInteractions(successHook);
    }
}
