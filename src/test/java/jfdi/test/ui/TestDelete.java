package jfdi.test.ui;

import static org.junit.Assert.assertEquals;

import jfdi.ui.Constants;

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

}
