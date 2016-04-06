package jfdi.test.ui;

import jfdi.ui.Constants;

public class TestAlias extends UiTest {

    TestAlias(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        testAliasDone();
        testDuplicatedAlias();
        testInvalidParameter();
    }

    /*
     * Test a "alias reschedule res" command and check if a task can be properly
     * rescheduled with the "res" command.
     */
    public void testAliasDone() {

        main.addTask("test tonight");

        main.execute("alias reschedule resch");
        main.assertResponseMessage(String.format(Constants.CMD_SUCCESS_ALIAS, "resch", "reschedule"));

        main.execute("resch 1 tomorrow");
        main.assertResponseMessage(String.format(Constants.CMD_SUCCESS_RESCHEDULED, 1, 1));
    }

    /*
     * Test a "alias rename res" command and check if this is identified as a
     * duplicated aliasing error following the testAliasDone test.
     */
    public void testDuplicatedAlias() {
        main.execute("alias rename resch");
        main.assertErrorMessage(String.format(Constants.CMD_ERROR_CANT_ALIAS_DUPLICATED, "resch"));
    }

    /*
     * Test a "alias invalid parameter" command and check if this is identified
     * as a erroneous aliasing attempt with invalid parameters.
     */
    public void testInvalidParameter() {
        main.execute("alias invalid parameter");
        main.assertErrorMessage(String.format(Constants.CMD_ERROR_CANT_ALIAS_INVALID, "parameter", "invalid"));
    }

}
