// @@author A0130195M
package jfdi.test.logic.commands;

import jfdi.logic.interfaces.Command;
import jfdi.parser.InputParser;
import jfdi.storage.apis.AliasAttributes;
import jfdi.storage.apis.AliasDb;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.apis.TaskDb;
import jfdi.ui.UI;
import org.junit.After;
import org.junit.Before;
import org.mockito.Mock;

/**
 * @author Liu Xinan
 */
public abstract class CommonCommandTest {

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

    @Before
    public void setUp() throws Exception {
        Command.setUi(ui);
        Command.setMainStorage(mainStorage);
        Command.setTaskDb(taskDb);
        Command.setAliasDb(aliasDb);
        Command.setParser(parser);

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
    }

}
