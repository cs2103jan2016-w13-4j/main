package jfdi.test.ui;

import jfdi.ui.Constants;

public class TestMark extends UiTest {

    TestMark(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        main.addRandomTasks(10);
        testMarkSingle();
        testMarkMultiple();
        testMarkNonExistent();
    }

    private void testMarkSingle() {
        main.execute("mark 1");
        assertMarked(1);
    }

    private void testMarkMultiple() {
        main.execute("mark 8,9");
        assertMarked(8, 9);
    }

    private void testMarkNonExistent() {
        int taskId = 999;
        main.execute(String.format("mark %d", taskId));
        main.assertErrorMessage(String.format(Constants.CMD_ERROR_CANT_MARK_NO_ID, taskId));
    }

    private void assertMarked(int... ids) {
        // TODO: Assert that the given IDs are marked
    }

}
