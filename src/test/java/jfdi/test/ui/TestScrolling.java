//@@author A0129538W

package jfdi.test.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javafx.scene.input.KeyCode;

public class TestScrolling extends UiTest {

    TestScrolling(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        main.addRandomTasks(10);
        testScrolling();
    }

    private void testScrolling() {
        int originalFirstvisibleId = main.controller.getFirstVisibleId();
        assertTrue(originalFirstvisibleId > 0);
        main.type(KeyCode.PAGE_UP);
        assertEquals(originalFirstvisibleId - 1, main.controller.getFirstVisibleId());
        main.type(KeyCode.PAGE_DOWN);
        assertEquals(originalFirstvisibleId, main.controller.getFirstVisibleId());
    }

}
