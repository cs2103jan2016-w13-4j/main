package jfditests.parsertests;

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
    public void testValidAddInput() {
        Command c = parser.build("add poop");
        Assert.assertNotNull(c);
        Assert.assertTrue(c instanceof AddTaskCommand);
    }

    @Test
    public void testValidAddInputWithoutIdentifier() {
        Command c = parser.build("poop ");
        Assert.assertNotNull(c);
        Assert.assertTrue(c instanceof AddTaskCommand);
    }

}
