package jfdi.test.ui;

import jfdi.ui.Constants;

public class TestShowDirectory extends UiTest {

    TestShowDirectory(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        testShowDirectory();
    }

    /*
     * Test a simple "directory" command
     */
    public void testShowDirectory() {
        main.execute("directory");
        main.assertResponseMessage(String.format(Constants.CMD_SUCCESS_SHOWDIRECTORY, main.getDirectory()));
    }

}
