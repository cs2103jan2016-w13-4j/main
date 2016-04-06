package jfdi.test.logic.commands;

import jfdi.logic.commands.DirectoryCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class DirectoryCommandTest extends CommonCommandTest {

    @Test
    public void execute() throws Exception {
        DirectoryCommand command = new DirectoryCommand.Builder().build();

        command.execute();

        verify(mainStorage).getCurrentDirectory();
    }

    @Test
    public void undo() throws Exception {
        DirectoryCommand command = new DirectoryCommand.Builder().build();

        thrown.expect(AssertionError.class);
        command.undo();
    }

}
