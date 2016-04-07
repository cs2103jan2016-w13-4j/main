package jfdi.test.logic.commands;

import jfdi.logic.commands.HelpCommand;
import jfdi.logic.events.HelpRequestedEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class HelpCommandTest extends CommonCommandTest {

    @Test
    public void execute() throws Exception {
        HelpCommand command = new HelpCommand.Builder().build();

        command.execute();

        verify(eventBus).post(any(HelpRequestedEvent.class));
    }

    @Test
    public void undo() throws Exception {
        HelpCommand command = new HelpCommand.Builder().build();

        thrown.expect(AssertionError.class);
        command.undo();
    }

}
