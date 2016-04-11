//@@author A0129538W

package jfdi.test.ui;

import jfdi.ui.Constants;

public class TestInvalidCmd extends UiTest {

    TestInvalidCmd(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        testInvalidCmd();
    }

    /*
     * Test the case when an invalid command issue occurs
     */
    public void testInvalidCmd() {
        main.addTask("test");

        main.execute("rename test testing");
        main.assertWarningMessage(String.format(Constants.CMD_WARNING_DONTKNOW, "rename test testing"));

        main.execute("reschedule test 7pm");
        main.assertWarningMessage(String.format(Constants.CMD_WARNING_DONTKNOW, "reschedule test 7pm"));

        main.execute("delete test");
        main.assertWarningMessage(String.format(Constants.CMD_WARNING_DONTKNOW, "delete test"));

        main.execute("mark test");
        main.assertWarningMessage(String.format(Constants.CMD_WARNING_DONTKNOW, "mark test"));

        main.execute("unmark test");
        main.assertWarningMessage(String.format(Constants.CMD_WARNING_DONTKNOW, "unmark test"));

        main.execute("alias reschedule");
        main.assertWarningMessage(String.format(Constants.CMD_WARNING_DONTKNOW, "alias reschedule"));

        main.execute("unalias reschedule test");
        main.assertWarningMessage(String.format(Constants.CMD_WARNING_DONTKNOW, "unalias reschedule test"));
    }
}
