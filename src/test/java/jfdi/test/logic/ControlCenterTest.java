// @@author A0130195M

package jfdi.test.logic;

import com.google.common.eventbus.EventBus;
import jfdi.logic.ControlCenter;
import jfdi.logic.commands.ExitCommand;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.commands.ListCommand;
import jfdi.logic.events.FilesReplacedEvent;
import jfdi.logic.events.InitializationFailedEvent;
import jfdi.logic.events.InvalidCommandEvent;
import jfdi.logic.interfaces.Command;
import jfdi.parser.InputParser;
import jfdi.parser.exceptions.InvalidInputException;
import jfdi.storage.apis.AliasDb;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.apis.TaskDb;
import jfdi.storage.exceptions.FilesReplacedException;
import jfdi.storage.exceptions.InvalidFilePathException;
import jfdi.ui.UI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class ControlCenterTest {

    @Mock
    private InputParser parser;

    @Mock
    private MainStorage mainStorage;

    @Mock
    private TaskDb taskDb;

    @Mock
    private AliasDb aliasDb;

    @Mock
    private EventBus eventBus;

    @Mock
    private ListCommand ls;

    @Mock
    private ExitCommand exit;

    @Mock
    private InvalidCommand lol;

    @Before
    public void setUp() throws Exception {
        ControlCenter.setParser(parser);
        ControlCenter.setMainStorage(mainStorage);
        ControlCenter.setTaskDb(taskDb);
        ControlCenter.setAliasDb(aliasDb);
        ControlCenter.setEventBus(eventBus);

        when(parser.getAllCommandRegexes()).thenReturn(InputParser.getInstance().getAllCommandRegexes());
    }

    @After
    public void tearDown() throws Exception {
        ControlCenter.setParser(InputParser.getInstance());
        ControlCenter.setMainStorage(MainStorage.getInstance());
        ControlCenter.setTaskDb(TaskDb.getInstance());
        ControlCenter.setAliasDb(AliasDb.getInstance());
        ControlCenter.setEventBus(UI.getEventBus());
        ControlCenter.removeInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertSame(ControlCenter.getInstance(), ControlCenter.getInstance());
    }

    @Test
    public void testHandleInput_successful() throws Exception {
        when(parser.parse("list")).thenReturn(ls);
        when(parser.parse("lol")).thenReturn(lol);
        when(parser.parse("exit")).thenReturn(exit);

        ControlCenter cc = ControlCenter.getInstance();

        cc.handleInput("list");
        cc.handleInput("lol");
        cc.handleInput("exit");

        verify(ls).execute();
        verify(lol).execute();
        verify(exit).execute();
    }

    @Test
    public void testHandleInput_invalidInput() throws Exception {
        doThrow(InvalidInputException.class).when(parser).parse(anyString());

        EventBus eventBus = mock(EventBus.class);
        Command.setEventBus(eventBus);

        ControlCenter cc = ControlCenter.getInstance();

        cc.handleInput("never gonna give");

        verify(eventBus).post(any(InvalidCommandEvent.class));
        Command.setEventBus(UI.getEventBus());
    }

    @Test
    public void testInitStorage_filesReplaced() throws Exception {
        doThrow(FilesReplacedException.class).when(mainStorage).initialize();

        ControlCenter.getInstance();
        verify(eventBus).post(any(FilesReplacedEvent.class));
    }

    @Test
    public void testInitStorage_invalidPath() throws Exception {
        doThrow(InvalidFilePathException.class).when(mainStorage).initialize();

        ControlCenter.getInstance();
        verify(eventBus).post(any(InitializationFailedEvent.class));
    }

}
