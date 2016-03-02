package dummytests;

import dummy.DummyParser;
import jfdi.logic.commands.ExitCommand;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.commands.ListCommand;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

/**
 * @author Liu Xinan
 */
public class DummyParserTest {

    private DummyParser dummyParser = DummyParser.getInstance();

    @Test
    public void testGetInstance() throws Exception {
        assertSame(dummyParser, DummyParser.getInstance());
        assertSame(DummyParser.getInstance(), DummyParser.getInstance());
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

        assertArrayEquals(tags.toArray(),
                ((ListCommand) dummyParser.parse("list tag1 tag2 tag3")).getTags().toArray());
    }
}
