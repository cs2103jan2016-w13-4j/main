package jfdi.test.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javafx.scene.input.KeyCode;

public class TestSurprise extends UiTest {

    public TestSurprise(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        testSurprise();
    }

    private void testSurprise() {
        // TODO: assert feedbacks
        main.type(KeyCode.F6);
        main.assertOnBox(main.surpriseBox);
        main.assertOnTab(main.surpriseTab, "surprise");

        main.type(KeyCode.F5);
        main.assertOnBox(main.completedBox);
        main.assertOnTab(main.completedTab, "completed");

        main.execute("surprise!!");
        main.assertOnBox(main.surpriseBox);
        main.assertOnTab(main.surpriseTab, "surprise");

        main.execute("yay");
        main.assertOnBox(main.incompleteBox);
        main.assertOnTab(main.incompleteTab, "incomplete");
        assertFalse(main.surpriseOverlay.isVisible());

        main.type(KeyCode.F6);
        main.execute("nay");
        main.assertOnBox(main.surpriseBox);
        main.assertOnTab(main.surpriseTab, "surprise");
        assertTrue(main.surpriseOverlay.isVisible());
    }

}
