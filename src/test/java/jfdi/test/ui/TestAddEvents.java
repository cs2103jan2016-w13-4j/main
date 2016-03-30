package jfdi.test.ui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import jfdi.ui.Constants;

public class TestAddEvents extends TestMain {

    /*
     * Test a simple "add testing1" command and check if the task is added to
     * the incomplete list as an item, and check if the feedback displayed
     * matches the expected lines.
     */
    @Test
    public void testAddTaskDone() {

        int listSize = controller.importantList.size();
        StringProperty notiSize = new SimpleStringProperty(Integer.toString(listSize + 1));

        clickOn(cmdArea).type(KeyCode.BACK_SPACE).type(KeyCode.A).type(KeyCode.D).type(KeyCode.D).type(KeyCode.SPACE)
                .type(KeyCode.T).type(KeyCode.E).type(KeyCode.S).type(KeyCode.T).type(KeyCode.I).type(KeyCode.N)
                .type(KeyCode.G).type(KeyCode.DIGIT1).type(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("The feedback message does not match the intended result.",
                "J.F.D.I. : " + String.format(Constants.CMD_SUCCESS_ADDED, "testing1"), fbArea.getText());
        assertEquals("The task is not properly added to the display list as an item.", listSize + 1,
                controller.importantList.size());
        assertEquals("Notification is not updated accordingly", notiSize.getValue(),
                controller.incompletePlaceHdr.getValue());
    }

/*
 * Test an "add " command with empty description and check if the error is
 * correctly detected and reflected.
 */
/*
 * @Test public void testAddTaskEmpty() {
 *
 * int listSize = controller.importantList.size(); StringProperty notiSize =
 * new SimpleStringProperty(Integer.toString(listSize));
 *
 * clickOn(cmdArea).type(KeyCode.BACK_SPACE).type(KeyCode.A).type(KeyCode.D)
 * .type(KeyCode.D).type(KeyCode.SPACE) .type(KeyCode.ENTER);
 * WaitForAsyncUtils.waitForFxEvents(); sleep(1000);
 *
 * assertEquals("The feedback message does not match the intended result.",
 * Constants.CMD_ERROR_CANT_ADD_EMPTY, fbArea.getText()); assertEquals(
 * "The task is accidentally added to the display list as an item.",
 * listSize, controller.importantList.size()); assertEquals(
 * "Notification is accidentally updated", notiSize,
 * controller.incompletePlaceHdr); }
 *
 *
 * Test the "add testing1" command again to check if error for duplicated
 * task can be detected and reflected correctly
 *
 * @Test public void testAddTaskDuplicated() {
 *
 * int listSize = controller.importantList.size(); StringProperty notiSize =
 * new SimpleStringProperty(Integer.toString(listSize));
 *
 * clickOn(cmdArea).type(KeyCode.BACK_SPACE).type(KeyCode.A).type(KeyCode.D)
 * .type(KeyCode.D).type(KeyCode.SPACE)
 * .type(KeyCode.T).type(KeyCode.E).type(KeyCode.S).type(KeyCode.T).type(
 * KeyCode.I).type(KeyCode.N)
 * .type(KeyCode.G).type(KeyCode.DIGIT1).type(KeyCode.ENTER);
 * WaitForAsyncUtils.waitForFxEvents(); sleep(1000);
 *
 * assertEquals("The feedback message does not match the intended result.",
 * Constants.CMD_ERROR_CANT_ADD_DUPLICATE, fbArea.getText()); assertEquals(
 * "The task is accidentally added to the display list as an item.",
 * listSize, controller.importantList.size()); assertEquals(
 * "Notification is accidentally updated", notiSize,
 * controller.incompletePlaceHdr); }
 */

}
