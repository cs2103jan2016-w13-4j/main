package jfdi.test.ui;

import javafx.collections.ObservableList;
import jfdi.ui.Constants;

public class TestMark extends UiTest {

    TestMark(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        main.addRandomTasks(3);
        testMarkSingle();
        testMarkMultiple();
        testMarkNonExistent();
    }

    private void testMarkSingle() {
        main.execute("mark 1");
        assertMarked(1);
    }

    private void testMarkMultiple() {
        main.execute("mark 2, 3");
        assertMarked(2, 3);
    }

    private void testMarkNonExistent() {
        int taskId = 999;
        main.execute(String.format("mark %d", taskId));
        main.assertErrorMessage(String.format(Constants.CMD_ERROR_CANT_MARK_NO_ID, taskId));
        main.execute("list completed");
    }

    private void assertMarked(int... ids) {
        for (int id : ids) {
            getStyleClass(id).contains("itemBoxDone");
        }
    }

    private ObservableList<String> getStyleClass(int onScreenId) {
        return main.controller.importantList.get(onScreenId - 1).getStyleClass();
    }

}
