package jfdi.test.parser;

import jfdi.logic.commands.AddTaskCommand;
import jfdi.logic.commands.ListCommand;
import jfdi.logic.interfaces.Command;
import jfdi.parser.InputParser;
import jfdi.parser.exceptions.InvalidInputException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    InputParser parser;

    @Before
    public void setupParser() {
        parser = InputParser.getInstance();
    }

    @Test
    public void testUserInputList() {
        String listCommand = "List";
        try {
            Command command = parser.parse(listCommand);
            Assert.assertTrue(command instanceof ListCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
    }

    @Test
    public void testUserInputAdd() {
        String addCommand = "add hello";
        try {
            Command command = parser.parse(addCommand);
            Assert.assertTrue(command instanceof AddTaskCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
    }
}
