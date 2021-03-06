// @@author A0130195M

package jfdi.test.logic.commands;

import com.google.common.eventbus.EventBus;
import jfdi.logic.interfaces.Command;
import jfdi.parser.InputParser;
import jfdi.storage.apis.AliasAttributes;
import jfdi.storage.apis.AliasDb;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.apis.TaskDb;
import jfdi.ui.UI;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Liu Xinan
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class CommonCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    protected UI ui;

    @Mock
    protected MainStorage mainStorage;

    @Mock
    protected TaskDb taskDb;

    @Mock
    protected AliasDb aliasDb;

    @Mock
    protected InputParser parser;

    @Mock
    protected EventBus eventBus;

    @Before
    public void setUp() throws Exception {
        Command.setUi(ui);
        Command.setMainStorage(mainStorage);
        Command.setTaskDb(taskDb);
        Command.setAliasDb(aliasDb);
        Command.setParser(parser);
        Command.setEventBus(eventBus);

        AliasAttributes.setCommandRegex(InputParser.getInstance().getAllCommandRegexes());
        InputParser.getInstance().setAliases(AliasDb.getInstance().getAll());
        MainStorage.getInstance().initialize();
        MainStorage.getInstance().use("./.test_data");
    }

    @After
    public void tearDown() throws Exception {
        Command.setUi(UI.getInstance());
        Command.setMainStorage(MainStorage.getInstance());
        Command.setTaskDb(TaskDb.getInstance());
        Command.setAliasDb(AliasDb.getInstance());
        Command.setParser(InputParser.getInstance());
        Command.setEventBus(UI.getEventBus());
    }

}
