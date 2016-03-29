// @@author A0127393B

package jfdi.test.parser;

import static org.junit.Assert.assertSame;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.commands.WildcardCommand;
import jfdi.logic.interfaces.Command;
import jfdi.parser.commandparsers.AbstractCommandParser;
import jfdi.parser.commandparsers.WildcardCommandParser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WildcardCommandParserTest {

    AbstractCommandParser parser = WildcardCommandParser.getInstance();

    @Before
    public void init() {
        parser = WildcardCommandParser.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertSame(parser, WildcardCommandParser.getInstance());
    }

    @Test
    public void testValidCommand() {
        Command command = parser.build("surprise");
        Assert.assertTrue(command instanceof WildcardCommand);
        command = parser.build("surprise!");
        Assert.assertTrue(command instanceof WildcardCommand);
        command = parser.build("Surprise!!!");
        Assert.assertTrue(command instanceof WildcardCommand);
        command = parser.build("suRPriSe!!!!!");
        Assert.assertTrue(command instanceof WildcardCommand);
    }

    @Test
    public void testInvalidCommand() {
        // Multiple words
        Command command = parser.build("surprise me");
        Assert.assertTrue(command instanceof InvalidCommand);
        command = parser.build("surprise! me too");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: wrong command
        command = parser.build("add 1-8");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: empty string
        command = parser.build("");
        Assert.assertTrue(command instanceof InvalidCommand);

        // Boundary case: null
        try {
            command = parser.build(null);
        } catch (AssertionError e) {
            Assert.assertTrue(true);
        }
    }

}
