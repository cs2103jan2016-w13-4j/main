//@@author A0129538W

package jfdi.test.ui;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.exceptions.FilesReplacedException;
import jfdi.storage.exceptions.InvalidFilePathException;
import jfdi.ui.Constants;

public class TestInitialization extends UiTest {

    TestInitialization(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        testFilesReplacedEvent();
        testInitializationFail();
    }

    /*
     * Test the case when the directory is occupied by other files and check if
     * a new location is created as the directory.
     */
    public void testFilesReplacedEvent() {
        // to be done
    }

    /*
     * Test the case when the initialization fail due to an invalid directory
     * file path.
     */
    public void testInitializationFail() {

        MainStorage mainStorage = mock(MainStorage.class);
        try {
            doThrow(InvalidFilePathException.class).when(mainStorage).use(testDir);
        } catch (InvalidFilePathException | FilesReplacedException e) {
            e.printStackTrace();
        }

        Command.setMainStorage(mainStorage);

        main.assertErrorMessage(String.format(Constants.CMD_ERROR_INIT_FAIL_INVALID, testDir));

        Command.setMainStorage(MainStorage.getInstance());
    }

}
