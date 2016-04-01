// @@author A0127393B

package jfdi.test.parser;

import static org.junit.Assert.assertSame;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.commands.UnaliasCommand;
import jfdi.logic.interfaces.Command;
import jfdi.parser.commandparsers.AbstractCommandParser;
import jfdi.parser.commandparsers.UnaliasCommandParser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UnaliasCommandParserTest {

    AbstractCommandParser parser = UnaliasCommandParser.getInstance();

    @Before
    public void init() {
        parser = UnaliasCommandParser.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertSame(parser, UnaliasCommandParser.getInstance());
    }

    @Test
    public void testValidCommand() {
        Command command = parser.build("unalias happy");
        Assert.assertTrue(command instanceof UnaliasCommand);
        UnaliasCommand unaliasCommand = (UnaliasCommand) command;
        assertSameAlias(unaliasCommand.getAlias(), "happy");

        command = parser.build("unalias 12345");
        Assert.assertTrue(command instanceof UnaliasCommand);
        unaliasCommand = (UnaliasCommand) command;
        assertSameAlias(unaliasCommand.getAlias(), "12345");

        command = parser.build("unalias q");
        Assert.assertTrue(command instanceof UnaliasCommand);
        unaliasCommand = (UnaliasCommand) command;
        assertSameAlias(unaliasCommand.getAlias(), "q");
    }

    @Test
    public void testInvalidCommand() {

        // Boundary case: wrong command
        Command command = parser.build("delete 1-5");
        Assert.assertTrue(command instanceof InvalidCommand);
        command = parser.build("add hello");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: no arguments
        command = parser.build("search");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: empty string
        command = parser.build("");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: null
        command = parser.build(null);
        Assert.assertTrue(command instanceof InvalidCommand);
    }

    private void assertSameAlias(String alias, String expected) {
        Assert.assertTrue(alias.equals(expected));
    }

}
