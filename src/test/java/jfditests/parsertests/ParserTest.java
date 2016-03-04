package jfditests.parsertests;

import jfdi.logic.interfaces.AbstractCommand;
import jfdi.parser.InputParser;

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
        AbstractCommand command = parser.parse(listCommand);
    }
}

