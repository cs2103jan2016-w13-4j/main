package jfdi.test.logic.commands;

import jfdi.logic.commands.ExitCommand;
import jfdi.logic.events.ExitCalledEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class ExitCommandTest extends CommonCommandTest {

    @Test
    public void execute() throws Exception {
        ExitCommand command = new ExitCommand.Builder().build();

        command.execute();

        verify(eventBus).post(any(ExitCalledEvent.class));
    }

    @Test
    public void undo() throws Exception {
        ExitCommand command = new ExitCommand.Builder().build();

        thrown.expect(AssertionError.class);
        command.undo();
    }

}
