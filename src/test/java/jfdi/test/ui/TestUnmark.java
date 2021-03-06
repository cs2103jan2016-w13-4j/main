//@@author A0129538W

package jfdi.test.ui;

import javafx.collections.ObservableList;
import jfdi.ui.Constants;

public class TestUnmark extends UiTest {

    public TestUnmark(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        main.addRandomTasks(3);
        main.execute("mark 1-3");
        testUnmarkSingle();
        testUnmarkMultiple();
        testUnmarkNonExistent();
    }

    private void testUnmarkSingle() {
        main.execute("unmark 1");
        assertUnmarked(1);
    }

    private void testUnmarkMultiple() {
        main.execute("unmark 2,3");
        assertUnmarked(2, 3);

        main.execute("mark 1-3");

        main.execute("unmark 1-3");
        assertUnmarked(1, 2, 3);
    }

    private void testUnmarkNonExistent() {
        int taskId = 999;
        main.execute(String.format("unmark %d", taskId));
        main.assertErrorMessage(String.format(Constants.CMD_ERROR_CANT_UNMARK_NO_ID, taskId));
    }

    private void assertUnmarked(int... ids) {
        for (int id : ids) {
            getStyleClass(id).contains("itemBox");
        }
    }

    private ObservableList<String> getStyleClass(int onScreenId) {
        return main.controller.importantList.get(onScreenId - 1).getStyleClass();
    }

}
