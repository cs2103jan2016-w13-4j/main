//@@author A0129538W

package jfdi.test.ui;

import static org.junit.Assert.assertEquals;

import jfdi.ui.Constants;
import jfdi.ui.Constants.ListStatus;

public class TestDelete extends UiTest {

    public TestDelete(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        main.addRandomTasks(5);
        testDeleteSingle();
        testDeleteMultiple();
        testDeleteNonExistent();
        testDeleteSearch();
    }

    private void testDeleteSingle() {
        int originalListSize = main.getImportantListSize();
        main.execute("delete 1");
        assertEquals(originalListSize - 1, main.getImportantListSize());
    }

    private void testDeleteMultiple() {
        int originalListSize = main.getImportantListSize();
        main.execute("delete 1, 2, 3");
        assertEquals(originalListSize - 3, main.getImportantListSize());
    }

    private void testDeleteNonExistent() {
        main.execute("delete 999");
        main.assertErrorMessage(Constants.CMD_ERROR_CANT_DELETE_NO_ID);
    }

    private void testDeleteSearch() {
        main.addTask("test 1");
        main.addTask("test 2");
        main.addTask("test 3");

        main.execute("search test");
        main.execute("delete 1");

        assertEquals(2, main.getImportantListSize());
        assertEquals(ListStatus.SEARCH.toString(), main.controller.displayStatus.toString());

    }

}
