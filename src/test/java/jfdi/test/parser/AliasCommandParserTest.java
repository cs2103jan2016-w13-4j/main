// @@author A0127393B

package jfdi.test.parser;

import static org.junit.Assert.assertSame;
import jfdi.logic.commands.AliasCommand;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.interfaces.Command;
import jfdi.parser.commandparsers.AbstractCommandParser;
import jfdi.parser.commandparsers.AliasCommandParser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AliasCommandParserTest {

    AbstractCommandParser parser = AliasCommandParser.getInstance();

    @Before
    public void init() {
        parser = AliasCommandParser.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertSame(parser, AliasCommandParser.getInstance());
    }

    @Test
    public void testValidCommand() {
        Command command = parser.build("alias add happy");
        Assert.assertTrue(command instanceof AliasCommand);
        AliasCommand aliasCommand = (AliasCommand) command;
        assertSameFields(aliasCommand, "add", "happy", true);

        command = parser.build("alias delete 12345");
        Assert.assertTrue(command instanceof AliasCommand);
        aliasCommand = (AliasCommand) command;
        assertSameFields(aliasCommand, "delete", "12345", true);

        command = parser.build("alias haha delete");
        Assert.assertTrue(command instanceof AliasCommand);
        aliasCommand = (AliasCommand) command;
        assertSameFields(aliasCommand, "haha", "delete", false);
    }

    @Test
    public void testInvalidCommand() {

        // Boundary case: wrong command
        Command command = parser.build("delete 1-5");
        Assert.assertTrue(command instanceof InvalidCommand);
        command = parser.build("add hello");
        Assert.assertTrue(command instanceof InvalidCommand);
        command = parser.build("add hello 3words");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: insufficient arguments
        command = parser.build("alias");
        Assert.assertTrue(command instanceof InvalidCommand);
        command = parser.build("alias oneword");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: empty string
        command = parser.build("");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: null
        command = parser.build(null);
        Assert.assertTrue(command instanceof InvalidCommand);
    }

    private void assertSameFields(AliasCommand aliasCommand, String command, String alias, boolean isValid) {
        Assert.assertEquals(aliasCommand.getCommand(), command);
        Assert.assertEquals(aliasCommand.getAlias(), alias);
        Assert.assertEquals(aliasCommand.isValid(), isValid);
    }

}
