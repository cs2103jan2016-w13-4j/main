package jfditests.parsertests;

import jfdi.parser.Parser;

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
    }
}

