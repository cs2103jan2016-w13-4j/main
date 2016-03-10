package dummytests;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import jfdi.logic.commands.ExitCommand;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.commands.ListCommand;

import org.junit.Test;

import dummy.InputParser;

/**
 * @author Liu Xinan
 */
public class DummyParserTest {

    private InputParser dummyParser = InputParser.getInstance();

    @Test
    public void testGetInstance() throws Exception {
        assertSame(dummyParser, InputParser.getInstance());
        assertSame(InputParser.getInstance(), InputParser.getInstance());
    }

    @Test
    public void testParse() throws Exception {
        assertThat(dummyParser.parse("list"), instanceOf(ListCommand.class));
        assertThat(dummyParser.parse("exit"), instanceOf(ExitCommand.class));
        assertThat(dummyParser.parse("lol"), instanceOf(InvalidCommand.class));
        assertThat(dummyParser.parse(""), instanceOf(InvalidCommand.class));

        ArrayList<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        tags.add("tag3");

        /*
         * assertArrayEquals(tags.toArray(), ((ListCommand)
         * dummyParser.parse("list tag1 tag2 tag3")).getTags().toArray());
         */
    }
}
