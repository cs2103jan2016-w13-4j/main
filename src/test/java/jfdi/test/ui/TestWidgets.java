package jfdi.test.ui;

import static org.junit.Assert.assertNotNull;

public class TestWidgets extends UiTest {

    TestWidgets(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        testWidgetsExist();
    }

    public void testWidgetsExist() {

        final String errMsg = " %s cannot be retrieved!";

        assertNotNull(String.format(errMsg, "incompleteBox"), main.incompleteBox);
        assertNotNull(String.format(errMsg, "overdueBox"), main.overdueBox);
        assertNotNull(String.format(errMsg, "upcomingBox"), main.upcomingBox);
        assertNotNull(String.format(errMsg, "allBox"), main.allBox);
        assertNotNull(String.format(errMsg, "completedBox"), main.completedBox);
        assertNotNull(String.format(errMsg, "searchBox"), main.searchBox);
        assertNotNull(String.format(errMsg, "surpriseBox"), main.surpriseBox);
        assertNotNull(String.format(errMsg, "helpBox"), main.helpBox);
        assertNotNull(String.format(errMsg, "incompleteTab"), main.incompleteTab);
        assertNotNull(String.format(errMsg, "overdueTab"), main.overdueTab);
        assertNotNull(String.format(errMsg, "upcomingTab"), main.upcomingTab);
        assertNotNull(String.format(errMsg, "allTab"), main.allTab);
        assertNotNull(String.format(errMsg, "completedTab"), main.completedTab);
        assertNotNull(String.format(errMsg, "searchTab"), main.searchTab);
        assertNotNull(String.format(errMsg, "surpriseTab"), main.surpriseTab);
        assertNotNull(String.format(errMsg, "helpTab"), main.helpTab);
        assertNotNull(String.format(errMsg, "incompleteCount"), main.incompleteCount);
        assertNotNull(String.format(errMsg, "overdueCount"), main.overdueCount);
        assertNotNull(String.format(errMsg, "upcomingCount"), main.upcomingCount);
        assertNotNull(String.format(errMsg, "dayDisplayer"), main.dayDisplayer);
        assertNotNull(String.format(errMsg, "listMain"), main.listMain);
        assertNotNull(String.format(errMsg, "fbArea"), main.fbArea);
        assertNotNull(String.format(errMsg, "cmdArea"), main.cmdArea);
        assertNotNull(String.format(errMsg, "helpContent"), main.helpContent);
        assertNotNull(String.format(errMsg, "surpriseOverlay"), main.surpriseOverlay);
        assertNotNull(String.format(errMsg, "surpriseTitle"), main.surpriseTitle);
        assertNotNull(String.format(errMsg, "taskDesc"), main.taskDesc);
        assertNotNull(String.format(errMsg, "taskTime"), main.taskTime);
        assertNotNull(String.format(errMsg, "surpriseBottom"), main.surpriseBottom);
        assertNotNull(String.format(errMsg, "noSurpriseOverlay"), main.noSurpriseOverlay);
    }
}
