package jfdi.test.ui;

import static org.junit.Assert.assertEquals;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import jfdi.ui.Constants;

public class TestAdd extends UiTest {

    TestAdd (TestMain main) {
        super(main);
    }

    @Override
    void run() {
        testAddTaskDone();
        testAddDuplicateTask();
        testAddEmptyTask();
        testAddUpcoming();
        testAddOverdue();
        testAddToCompleted();
        testAddToAll();
        testAddToSurprise();
        testAddToSearch();
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

        main.addTask(taskName);

        main.assertResponseMessage(String.format(Constants.CMD_SUCCESS_ADDED, 1, taskName));
        assertEquals(listSize + 1, main.getImportantListSize());
        assertEquals(notiSize + 1, Integer.parseInt(main.controller.incompletePlaceHdr.getValue()));
    }

    public void testAddUpcoming() {
        main.execute("list upcoming");
        int originalListSize = main.getImportantListSize();
        int originalNotiSize = Integer.parseInt(main.controller.upcomingPlaceHdr.getValueSafe());
        
        main.assertOnBox(main.upcomingBox);
        main.assertOnTab(main.upcomingTab, "upcoming");
        
        main.addTask("something 2 hours later");
        main.addTask("something 1 hour later");

        assertEquals(originalListSize + 2, main.getImportantListSize());
        assertEquals(originalNotiSize + 2, Integer.parseInt(main.controller.upcomingPlaceHdr.getValue()));
        
        main.assertOnBox(main.upcomingBox);
        main.assertOnTab(main.upcomingTab, "upcoming");
        
        String taskName = "testing2";
        main.addTask(taskName);
        
        main.assertOnBox(main.incompleteBox);
        main.assertOnTab(main.incompleteTab, "incomplete");
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

    /*
     * Test an "add" command on the OVERDUE list to see if the new item is added
     * to the correct list.
     */
    public void testAddOverdue() {
        
        main.type(KeyCode.F2);
        main.assertOnBox(main.overdueBox);
        main.assertOnTab(main.overdueTab, "overdue");
        
        int originalListSize = main.getImportantListSize();
        int originalNotiSize = Integer.parseInt(main.controller.overduePlaceHdr.getValueSafe());
        
        main.addTask("something yesterday");
        main.addTask("something last week");

        assertEquals(originalListSize + 2, main.getImportantListSize());
        assertEquals(originalNotiSize + 2, Integer.parseInt(main.controller.upcomingPlaceHdr.getValue()));
        
        main.assertOnBox(main.overdueBox);
        main.assertOnTab(main.overdueTab, "overdue");
        
        String taskName = "testing3";
        main.addTask(taskName);
        
        main.assertOnBox(main.incompleteBox);
        main.assertOnTab(main.incompleteTab, "incomplete");
    }
    
    /*
     * Test an "add" command on the COMPLETED list to see if the new item is
     * added to the correct list.
     */
    public void testAddToCompleted() {
        main.type(KeyCode.F5);
        main.assertOnBox(main.completedBox);
        main.assertOnTab(main.completedTab, "completed");
        
        String taskName = "testing4";
        main.addTask(taskName);
        
        main.assertOnBox(main.incompleteBox);
        main.assertOnTab(main.incompleteTab, "incomplete");
    }
    
    /*
     * Test an "add" command on the ALL list to see if the new item is
     * added to the correct list.
     */
    public void testAddToAll() {
        main.type(KeyCode.F4);
        main.assertOnBox(main.allBox);
        main.assertOnTab(main.allTab, "all");
        
        int originalListSize = main.getImportantListSize();
        
        String taskName = "testing5";
        main.addTask(taskName);
        
        main.assertOnBox(main.allBox);
        main.assertOnTab(main.allTab, "all");
        
        assertEquals(originalListSize + 1, main.getImportantListSize());
    }
    
    
    /*
     * Test an "add" command on the SURPRISE list to see if the new item is
     * added to the correct list.
     */
    public void testAddToSurprise() {
        main.type(KeyCode.F6);
        main.assertOnBox(main.surpriseBox);
        main.assertOnTab(main.surpriseTab, "surprise");
        
        String taskName = "testing6";
        main.addTask(taskName);
        
        main.assertOnBox(main.incompleteBox);
        main.assertOnTab(main.incompleteTab, "incomplete");
    }
    
    /*
     * Test an "add" command on the SEARCH list to see if the new item is
     * added to the correct list.
     */
    public void testAddToSearch() {
        main.execute("Search testing");
        main.assertOnBox(main.searchBox);
        main.assertOnTab(main.searchTab, "search");
        
        String taskName = "testing7";
        main.addTask(taskName);
        
        main.assertOnBox(main.incompleteBox);
        main.assertOnTab(main.incompleteTab, "incomplete");
    }
}
