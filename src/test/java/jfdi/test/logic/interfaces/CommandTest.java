// @@author A0130195M

package jfdi.test.logic.interfaces;

import com.google.common.eventbus.EventBus;
import jfdi.logic.interfaces.Command;
import jfdi.parser.InputParser;
import jfdi.storage.apis.AliasDb;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.apis.TaskDb;
import jfdi.ui.UI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public class CommandTest {

    private Command command;

    @Mock
    private UI ui;

    @Mock
    private MainStorage mainStorage;

    @Mock
    private TaskDb taskDb;

    @Mock
    private AliasDb aliasDb;

    @Mock
    private InputParser parser;

    @Mock
    private EventBus eventBus;

    @Before
    public void setUp() throws Exception {
        command = Mockito.mock(Command.class, Mockito.CALLS_REAL_METHODS);
        Command.setUi(ui);
        Command.setMainStorage(mainStorage);
        Command.setTaskDb(taskDb);
        Command.setAliasDb(aliasDb);
        Command.setParser(parser);
        Command.setEventBus(eventBus);
    }

    @After
    public void tearDown() throws Exception {
        Command.setUi(UI.getInstance());
        Command.setMainStorage(MainStorage.getInstance());
        Command.setTaskDb(TaskDb.getInstance());
        Command.setAliasDb(AliasDb.getInstance());
        Command.setParser(InputParser.getInstance());
        Command.setEventBus(UI.getEventBus());
        Command.setRedoing(false);
    }

    @Test
    public void testRedoing() throws Exception {
        Command.setRedoing(true);
        assertEquals(true, Command.isRedoing());
        Command.setRedoing(false);
        assertEquals(false, Command.isRedoing());
    }

    @Test
    public void pushToUndoStack() throws Exception {
        int currentSize = Command.getUndoStack().size();

        command.pushToUndoStack();
        assertEquals(currentSize + 1, Command.getUndoStack().size());

        command.pushToUndoStack();
        assertEquals(currentSize + 2, Command.getUndoStack().size());

        command.pushToRedoStack();
        command.pushToUndoStack();
        assertEquals(currentSize + 4, Command.getUndoStack().size());
    }

    @Test
    public void pushToRedoStack() throws Exception {
        int currentSize = Command.getRedoStack().size();

        command.pushToRedoStack();
        assertEquals(currentSize + 1, Command.getRedoStack().size());

        command.pushToRedoStack();
        assertEquals(currentSize + 2, Command.getRedoStack().size());

        command.pushToRedoStack();
        assertEquals(currentSize + 3, Command.getRedoStack().size());
    }

    @Test
    public void getParser() throws Exception {
        assertSame(parser, Command.getParser());
    }

    @Test
    public void getMainStorage() throws Exception {
        assertSame(mainStorage, Command.getMainStorage());
    }

    @Test
    public void getTaskDb() throws Exception {
        assertSame(taskDb, Command.getTaskDb());
    }

    @Test
    public void getAliasDb() throws Exception {
        assertSame(aliasDb, Command.getAliasDb());
    }

    @Test
    public void getUI()  throws Exception {
        assertSame(ui, Command.getUI());
    }

    @Test
    public void getEventBus() throws Exception {
        assertSame(eventBus, Command.getEventBus());
    }

}
