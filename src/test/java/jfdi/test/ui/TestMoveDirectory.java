//@@author A0129538W

package jfdi.test.ui;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import com.google.common.io.Files;

import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.exceptions.FilesReplacedException;
import jfdi.storage.exceptions.InvalidFilePathException;
import jfdi.ui.Constants;

public class TestMoveDirectory extends UiTest {

    TestMoveDirectory(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        testMoveDirectory();
        testInvalidPath();
    }

    /*
     * Test a simple "move" command
     */
    public void testMoveDirectory() {
        String newDir = Files.createTempDir().getAbsolutePath();
        main.execute("move " + newDir);
        main.assertResponseMessage(String.format(Constants.CMD_SUCCESS_MOVED, newDir));
    }

    /*
     * Test a simple "move" command with an invalid path name to check if the
     * error returned is correct.
     */
    public void testInvalidPath() {

        String newDir = Files.createTempDir().getAbsolutePath();

        MainStorage mainStorage = mock(MainStorage.class);
        try {
            doThrow(InvalidFilePathException.class).when(mainStorage).changeDirectory(newDir);
        } catch (InvalidFilePathException | FilesReplacedException e) {
            e.printStackTrace();
        }

        Command.setMainStorage(mainStorage);

        main.execute("move " + newDir);
        main.assertErrorMessage(String.format(Constants.CMD_ERROR_MOVE_FAIL_INVALID, newDir));

        Command.setMainStorage(MainStorage.getInstance());
    }

}
