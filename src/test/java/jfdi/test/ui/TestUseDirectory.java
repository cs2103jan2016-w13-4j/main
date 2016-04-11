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

public class TestUseDirectory extends UiTest {

    TestUseDirectory(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        testInvalidPath();
    }

    /*
     * Test a simple "use" command with an invalid path name to check if the
     * error returned is correct.
     */
    public void testInvalidPath() {

        String newDir = Files.createTempDir().getAbsolutePath();

        MainStorage mainStorage = mock(MainStorage.class);
        try {
            doThrow(InvalidFilePathException.class).when(mainStorage).use(newDir);
        } catch (InvalidFilePathException | FilesReplacedException e) {
            e.printStackTrace();
        }

        Command.setMainStorage(mainStorage);

        main.execute("use " + newDir);
        main.assertErrorMessage(String.format(Constants.CMD_ERROR_USE_FAIL_INVALID, newDir));

        Command.setMainStorage(MainStorage.getInstance());
    }

}
