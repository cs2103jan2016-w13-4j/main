package jfdi.test.ui;

import javafx.scene.input.KeyCode;

public class TestList extends UiTest {

    TestList(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        testListIncomplete();
        testListOverdue();
        testListUpcoming();
        testListAll();
        testListCompleted();
    }

    private void testListIncomplete() {
        String tabName = "incomplete";
        list("");
        main.assertOnBox(main.incompleteBox);
        main.assertOnTab(main.incompleteTab, tabName);

        list(tabName);
        main.assertOnBox(main.incompleteBox);
        main.assertOnTab(main.incompleteTab, tabName);

        main.type(KeyCode.F1);
        main.assertOnBox(main.incompleteBox);
        main.assertOnTab(main.incompleteTab, tabName);
    }

    private void testListOverdue() {
        String tabName = "overdue";
        list(tabName);
        main.assertOnBox(main.overdueBox);
        main.assertOnTab(main.overdueTab, tabName);

        main.type(KeyCode.F2);
        main.assertOnBox(main.overdueBox);
        main.assertOnTab(main.overdueTab, tabName);
    }

    private void testListUpcoming() {
        String tabName = "upcoming";
        list(tabName);
        main.assertOnBox(main.upcomingBox);
        main.assertOnTab(main.upcomingTab, tabName);

        main.type(KeyCode.F3);
        main.assertOnBox(main.upcomingBox);
        main.assertOnTab(main.upcomingTab, tabName);
    }

    private void testListAll() {
        String tabName = "all";
        list(tabName);
        main.assertOnBox(main.allBox);
        main.assertOnTab(main.allTab, tabName);

        main.type(KeyCode.F4);
        main.assertOnBox(main.allBox);
        main.assertOnTab(main.allTab, tabName);
    }

    private void testListCompleted() {
        String tabName = "completed";
        list(tabName);
        main.assertOnBox(main.completedBox);
        main.assertOnTab(main.completedTab, tabName);

        main.type(KeyCode.F5);
        main.assertOnBox(main.completedBox);
        main.assertOnTab(main.completedTab, tabName);
    }

    private void list(String name) {
        main.execute("list " + name);
    }

}
