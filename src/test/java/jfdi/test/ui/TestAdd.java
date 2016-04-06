package jfdi.test.ui;

import static org.junit.Assert.assertEquals;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jfdi.ui.Constants;

public class TestAdd extends UiTest {

    TestAdd(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        testAddTaskDone();
        testAddDuplicateTask();
        testAddEmptyTask();
        testAddUpcoming();
    }


    /*
     * Test a simple "add testing1" command and check if the task is added to
     * the incomplete list as an item, and check if the feedback displayed
     * matches the expected lines.
     */
    public void testAddTaskDone() {
        String taskName = "testing1";
        int listSize = main.getImportantListSize();
        int notiSize = Integer.parseInt(main.controller.upcomingPlaceHdr.getValueSafe());
        //StringProperty notiSize = new SimpleStringProperty(Integer.toString(listSize));

        main.addTask(taskName);

        main.assertResponseMessage(String.format(Constants.CMD_SUCCESS_ADDED, 1, taskName));
        assertEquals(listSize + 1, main.getImportantListSize());
        assertEquals(notiSize + 1, Integer.parseInt(main.controller.incompletePlaceHdr.getValue()));
    }

    public void testAddUpcoming() {
        main.execute("list upcoming");
        int originalListSize = main.getImportantListSize();
        int originalNotiSize = Integer.parseInt(main.controller.upcomingPlaceHdr.getValueSafe());

        main.addTask("something 2 hours later");
        main.addTask("something 1 hour later");

        assertEquals(originalListSize + 2, main.getImportantListSize());
        assertEquals(originalNotiSize + 2, Integer.parseInt(main.controller.upcomingPlaceHdr.getValue()));
    }

    /*
     * Test the "add testing1" command again to check if error for duplicated
     * task can be detected and reflected correctly
     */
    public void testAddDuplicateTask() {
        int listSize = main.getImportantListSize();
        StringProperty notiSize = new SimpleStringProperty(Integer.toString(listSize));

        String taskName = "testing1";
        main.addTask(taskName);

        main.assertErrorMessage(Constants.CMD_ERROR_CANT_ADD_DUPLICATE);
        assertEquals(listSize, main.getImportantListSize());
        assertEquals(notiSize.getValue(), main.controller.incompletePlaceHdr.getValue());
    }

    /*
     * Test an "add " command with empty description and check if the error is
     * correctly detected and reflected.
     */
    public void testAddEmptyTask() {
        int listSize = main.getImportantListSize();
        StringProperty notiSize = new SimpleStringProperty(Integer.toString(listSize));

        main.addTask("\" \"");

        main.assertErrorMessage(Constants.CMD_ERROR_CANT_ADD_EMPTY);
        assertEquals(listSize, main.getImportantListSize());
        assertEquals(notiSize.getValue(), main.controller.incompletePlaceHdr.getValue());
    }
}
