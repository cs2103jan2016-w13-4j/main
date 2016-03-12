package jfdi.test.parser;

import static org.junit.Assert.assertSame;
import jfdi.logic.commands.AddTaskCommand;
import jfdi.logic.interfaces.Command;
import jfdi.parser.commandparsers.AbstractCommandParser;
import jfdi.parser.commandparsers.AddCommandParser;

import org.junit.Assert;
import org.junit.Test;
import org.testng.annotations.BeforeClass;

public class AddCommandParserTest {
    AbstractCommandParser parser = AddCommandParser.getInstance();

    @BeforeClass
    public void init() {
        parser = AddCommandParser.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertSame(parser, AddCommandParser.getInstance());
    }

    @Test
    public void testValidAddInput() {
        Command addCommand = parser.build("add hello");
        Assert.assertNotNull(addCommand);
        Assert.assertTrue(addCommand instanceof AddTaskCommand);
    }

    @Test
    public void testValidAddInputWithoutIdentifier() {
        Command addCommand = parser.build("poop ");
        Assert.assertNotNull(addCommand);
        Assert.assertTrue(addCommand instanceof AddTaskCommand);
    }

}
