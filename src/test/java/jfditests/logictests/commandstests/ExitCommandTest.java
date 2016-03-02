package jfditests.logictests.commandstests;

import jfdi.logic.commands.ExitCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.function.Consumer;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class ExitCommandTest {

    @Mock private Consumer<ExitCommand> successHook;
    @Mock private Consumer<ExitCommand> failureHook;

    private ExitCommand exitCommand = new ExitCommand.Builder().build();

    @Test
    public void testExitCommand() throws Exception {
        ExitCommand.addSuccessHook(successHook);
        ExitCommand.addFailureHook(failureHook);

        exitCommand.execute();

        verify(successHook).accept(exitCommand);
        // ExitCommand cannot fail.
        verifyNoMoreInteractions(failureHook);
    }

}
