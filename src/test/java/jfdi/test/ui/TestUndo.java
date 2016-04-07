package jfdi.test.ui;

import static org.junit.Assert.assertEquals;

import jfdi.logic.interfaces.Command;
import jfdi.ui.Constants;

public class TestUndo extends UiTest {

    TestUndo(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        testUndoNothing();
        testUndoDone();
    }

    /*
     * Test a simple "undo" command when there is no prior commands to check if
     * the correct error message is returned.
     */
    public void testUndoNothing() {
        main.controller.clearInputHistory();
        Command.clearUndoStack();
        main.execute("undo");
        main.assertErrorMessage(Constants.CMD_ERROR_UNDO_FAIL_NO_TASKS);
    }

    /*
     * Test a simple "undo" command and check if the previous action is undone.
     */
    public void testUndoDone() {
        main.addRandomTasks(1);
        main.execute("undo");
        main.assertResponseMessage(Constants.CMD_SUCCESS_UNDONE);
        assertEquals(0, main.getImportantListSize());
    }

}
