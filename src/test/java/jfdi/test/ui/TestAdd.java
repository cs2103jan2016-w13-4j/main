package jfdi.test.ui;

import static org.junit.Assert.assertEquals;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jfdi.ui.Constants;

import org.testfx.util.WaitForAsyncUtils;

public class TestAdd {

    private static TestMain main;

    public static void run(TestMain testMain) {
        main = testMain;
        testAddTaskDone();
        testAddDuplicateTask();
        testAddEmptyTask();
    }

    /*
     * Test a simple "add testing1" command and check if the task is added to
     * the incomplete list as an item, and check if the feedback displayed
     * matches the expected lines.
     */
    public static void testAddTaskDone() {
        String taskName = "testing1";
        int listSize = main.controller.importantList.size();
        StringProperty notiSize = new SimpleStringProperty(Integer.toString(listSize + 1));
        String expectedFeedback = String.format(Constants.UI_MESSAGE_RESPONSE,
                String.format(Constants.CMD_SUCCESS_ADDED, taskName)).trim();

        addTask(taskName);

        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(expectedFeedback, main.fbArea.getText());
        assertEquals(listSize + 1, main.controller.importantList.size());
        assertEquals(notiSize.getValue(), main.controller.incompletePlaceHdr.getValue());
    }

    /*
     * Test the "add testing1" command again to check if error for duplicated
     * task can be detected and reflected correctly
     */
    public static void testAddDuplicateTask() {
        int listSize = main.controller.importantList.size();
        StringProperty notiSize = new SimpleStringProperty(Integer.toString(listSize));
        String expectedFeedback = String.format(Constants.UI_MESSAGE_ERROR,
                Constants.CMD_ERROR_CANT_ADD_DUPLICATE).trim();

        String taskName = "testing1";
        addTask(taskName);

        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(expectedFeedback, main.fbArea.getText());
        assertEquals(listSize, main.controller.importantList.size());
        assertEquals(notiSize.getValue(), main.controller.incompletePlaceHdr.getValue());
    }

    /*
     * Test an "add " command with empty description and check if the error is
     * correctly detected and reflected.
     */
    public static void testAddEmptyTask() {
        int listSize = main.controller.importantList.size();
        StringProperty notiSize = new SimpleStringProperty(Integer.toString(listSize));
        String expectedFeedback = String.format(Constants.UI_MESSAGE_ERROR,
                Constants.CMD_ERROR_CANT_ADD_EMPTY).trim();

        addTask("\" \"");

        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(expectedFeedback, main.fbArea.getText());
        assertEquals(listSize, main.controller.importantList.size());
        assertEquals(notiSize.getValue(), main.controller.incompletePlaceHdr.getValue());
    }

    /*
     * Helper method
     */
    private static void addTask(String taskName) {
        main.execute(String.format("add %s", taskName));
    }

}
