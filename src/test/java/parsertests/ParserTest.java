package parsertests;

import jfdi.parser.ActionType;
import jfdi.parser.Parser;
import command.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class ParserTest {
    Parser parser;

    @Before
    public void setupParser() {
        parser = Parser.getInstance();
    }

    @Test
    public void testUserInputAdd() {
        String userInput = ("Add poop");
        Command result = parser.parse(userInput);
        Assert.assertEquals(result.getAction(), new Command(ActionType.ADD,
                null, null, null, null, null).getAction());
    }
}