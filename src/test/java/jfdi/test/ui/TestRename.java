package jfdi.test.ui;

import static org.junit.Assert.assertTrue;

import jfdi.ui.Constants;

public class TestRename extends UiTest {

    TestRename (TestMain main) {
        super(main);
    }

    @Override
    void run() {
        testRenameDone();
        testNonExistentId();
        testNoChange();
        testDuplicatedTask();
    }

    /*
     * Test a simple "rename" command and check if the name/description
     * of the task is indeed changed.
     */
    public void testRenameDone() {
        
        assertTrue(main.getImportantListSize() == 0);
        
        main.addTask("testing1");
        
        main.execute("rename 1 testing2");
        main.assertResponseMessage(String.format(Constants.CMD_SUCCESS_RENAMED, 1, "testing2"));
        //assertEquals("testing2", main.listMain.getItems().get(1).getItem().getDescription());]
        
    }
    
    /*
     * Test a simple "rename" command with index 100 to check if the non-existent-ID
     * error is correctly returned
     */
    public void testNonExistentId() {
        
        main.execute("rename 100 same task");
        main.assertErrorMessage(String.format(Constants.CMD_ERROR_CANT_RENAME_NO_ID, 100));
    }
    
    /*
     * Test a simple "rename" command and check if an attempt to rename a task to the
     * current name returns a name-no-changes error
     */
    public void testNoChange() {
        
        main.addTask("same task");
        main.execute("rename 2 same task");
        main.assertErrorMessage(String.format(Constants.CMD_ERROR_CANT_RENAME_NO_CHANGES, "same task"));
    }
    
    /*
     * Test a simple "rename" command and check if an attempt to rename a task to an
     * existing name returns a duplicated-task error
     */
    public void testDuplicatedTask() {
        
        main.addTask("testing1");
        main.execute("rename 3 testing2");
        main.assertErrorMessage(Constants.CMD_ERROR_CANT_RENAME_DUPLICATE);
    }
}
