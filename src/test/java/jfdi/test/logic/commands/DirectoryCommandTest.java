package jfdi.test.logic.commands;

import jfdi.logic.commands.DirectoryCommand;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * @author Liu Xinan
 */
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
