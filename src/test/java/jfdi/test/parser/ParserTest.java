package jfdi.test.parser;

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
        } catch (InvalidInputException e) {
            Assert.fail();
        }
    }
}
