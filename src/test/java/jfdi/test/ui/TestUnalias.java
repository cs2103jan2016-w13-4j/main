package jfdi.test.ui;

import static org.junit.Assert.assertEquals;

import jfdi.ui.Constants;

public class TestUnalias extends UiTest {

    TestUnalias(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        testUnaliasDone();
        testNonExistentAlias();
    }

    /*
     * Test a simple "unalias" command and check if the non-existent alias will
     * be treated as a new task adding command.
     */
    public void testUnaliasDone() {
        main.addRandomTasks(2);
        main.execute("alias delete del");
        main.execute("del 1");
        main.assertResponseMessage(String.format(Constants.CMD_SUCCESS_DELETED_1, 1));
        assertEquals(1, main.getImportantListSize());
        
        main.execute("unalias del");
        main.execute("del 1");
        main.assertResponseMessage(String.format(Constants.CMD_SUCCESS_ADDED, 2, "del 1"));
        assertEquals(2, main.getImportantListSize());

    }
    
    public void testNonExistentAlias() {
        main.execute("unalias nonexistent");
        main.assertErrorMessage(String.format(Constants.CMD_ERROR_CANT_UNALIAS_NO_ALIAS, "nonexistent"));
    }

}
