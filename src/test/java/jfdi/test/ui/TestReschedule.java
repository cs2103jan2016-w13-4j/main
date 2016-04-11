//@@author A0129538W

package jfdi.test.ui;

import static org.junit.Assert.assertEquals;

import javafx.scene.input.KeyCode;
import jfdi.ui.Constants;
import jfdi.ui.Constants.ListStatus;

public class TestReschedule extends UiTest {

    TestReschedule(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        testReschedulTaskDone();
        testReschNonExistentID();
        testReschNoChange();
        testReschDuplicatedTask();
    }

    /*
     * Test a simple "reschedule" command and check if the time can be changed
     * properly in the correct list
     */
    public void testReschedulTaskDone() {

        // rescheduling in INCOMPLETE list is already tested in TestAlias.java

        // test in OVERDUE list
        main.type(KeyCode.F2);
        main.addTask("scheduling by yesterday 7am");

        main.execute("reschedule 1 by yesterday 9pm");
        main.assertResponseMessage(String.format(Constants.CMD_SUCCESS_RESCHEDULED, 1, 1));
        assertEquals(ListStatus.OVERDUE.toString(), main.controller.displayStatus.toString());
    }

    /*
     * Test a simple "reschedule" command that specifies a non existent index.
     */
    public void testReschNonExistentID() {

        assertEquals(ListStatus.OVERDUE.toString(), main.controller.displayStatus.toString());

        main.execute("reschedule 999 today");
        main.assertErrorMessage(String.format(Constants.CMD_ERROR_CANT_RESCHEDULE_NO_ID, 999));
    }

    /*
     * Test a simple "reschedule" command that makes no changes to the time of
     * the specified task.
     */
    public void testReschNoChange() {

        assertEquals(ListStatus.OVERDUE.toString(), main.controller.displayStatus.toString());

        main.execute("reschedule 1 yesterday 9pm");
        main.assertErrorMessage(Constants.CMD_ERROR_CANT_RESCHEDULE_NO_CHANGES);
    }

    /*
     * Test a simple "reschedule" command that changes the specified task to be
     * exactly the same as another task.
     */
    public void testReschDuplicatedTask() {

        assertEquals(ListStatus.OVERDUE.toString(), main.controller.displayStatus.toString());

        main.addTask("scheduling from yesterday 7am to 8pm");

        main.execute("reschedule 1 by yesterday 9pm");
        main.assertErrorMessage(Constants.CMD_ERROR_CANT_RESCHEDULE_DUPLICATE);
    }

}
