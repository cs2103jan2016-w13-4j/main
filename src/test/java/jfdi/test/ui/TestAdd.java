package jfdi.test.ui;

import static org.junit.Assert.assertEquals;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jfdi.ui.Constants;

import org.testfx.util.WaitForAsyncUtils;

public class TestAdd extends UiTest {

    TestAdd(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        testAddTaskDone();
        testAddDuplicateTask();
        testAddEmptyTask();
    }


    /*
     * Test a simple "add testing1" command and check if the task is added to
     * the incomplete list as an item, and check if the feedback displayed
     * matches the expected lines.
     */
    public void testAddTaskDone() {
        String taskName = "testing1";
        int listSize = main.controller.importantList.size();
        StringProperty notiSize = new SimpleStringProperty(Integer.toString(listSize + 1));

        main.addTask(taskName);

        WaitForAsyncUtils.waitForFxEvents();

        main.assertResponseMessage(String.format(Constants.CMD_SUCCESS_ADDED, taskName));
        assertEquals(listSize + 1, main.controller.importantList.size());
        assertEquals(notiSize.getValue(), main.controller.incompletePlaceHdr.getValue());
    }

    /*
     * Test the "add testing1" command again to check if error for duplicated
     * task can be detected and reflected correctly
     */
    public void testAddDuplicateTask() {
        int listSize = main.controller.importantList.size();
        StringProperty notiSize = new SimpleStringProperty(Integer.toString(listSize));

        String taskName = "testing1";
        main.addTask(taskName);

        WaitForAsyncUtils.waitForFxEvents();

        main.assertErrorMessage(Constants.CMD_ERROR_CANT_ADD_DUPLICATE);
        assertEquals(listSize, main.controller.importantList.size());
        assertEquals(notiSize.getValue(), main.controller.incompletePlaceHdr.getValue());
    }

    /*
     * Test an "add " command with empty description and check if the error is
     * correctly detected and reflected.
     */
    public void testAddEmptyTask() {
        int listSize = main.controller.importantList.size();
        StringProperty notiSize = new SimpleStringProperty(Integer.toString(listSize));

        main.addTask("\" \"");

        WaitForAsyncUtils.waitForFxEvents();
        main.assertErrorMessage(Constants.CMD_ERROR_CANT_ADD_EMPTY);
        assertEquals(listSize, main.controller.importantList.size());
        assertEquals(notiSize.getValue(), main.controller.incompletePlaceHdr.getValue());
    }

}
