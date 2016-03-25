package jfdi.test.logic;

import jfdi.logic.ControlCenter;
import jfdi.logic.commands.ExitCommand;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.commands.ListCommand;
import jfdi.parser.InputParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class ControlCenterTest {

    @Mock
    private InputParser parser;

    @Mock
    private ListCommand ls;

    @Mock
    private ExitCommand exit;

    @Mock
    private InvalidCommand lol;

    private ControlCenter cc = ControlCenter.getInstance();

    @Test
    public void testGetInstance() throws Exception {
        assertSame(ControlCenter.getInstance(), ControlCenter.getInstance());
    }

    @Test
    public void testHandleInput() throws Exception {
        cc.setParser(parser);

        when(parser.parse("list")).thenReturn(ls);
        when(parser.parse("lol")).thenReturn(lol);
        when(parser.parse("exit")).thenReturn(exit);

        cc.handleInput("list");
        cc.handleInput("lol");
        cc.handleInput("exit");

        verify(ls).execute();
        verify(lol).execute();
        verify(exit).execute();
    }

}
