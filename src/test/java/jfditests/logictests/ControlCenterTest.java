package jfditests.logictests;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import jfdi.logic.ControlCenter;
import jfdi.logic.commands.ExitCommand;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.commands.ListCommand;
import jfdi.parser.InputParser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Liu Xinan
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ InputParser.class })
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

    @Before
    public void setUp() throws Exception {
        mockStatic(InputParser.class);
        when(InputParser.getInstance()).thenReturn(parser);
    }

    @Test
    public void testGetInstance() throws Exception {
        assertSame(ControlCenter.getInstance(), ControlCenter.getInstance());
    }

    @Test
    public void testHandleInput() throws Exception {
        when(parser.parse(any())).thenReturn(ls, lol, exit);

        cc.handleInput("list");
        cc.handleInput("lol");
        cc.handleInput("exit");

        verify(ls).execute();
        verify(lol).execute();
        verify(exit).execute();
    }

}
