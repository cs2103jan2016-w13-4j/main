package jfdi.test.ui;

import static org.junit.Assert.assertEquals;

import org.testfx.util.WaitForAsyncUtils;

public class TestHelp {

    private static TestMain main;

    public static void run(TestMain testMain) {
        main = testMain;
        testHelpTaskDone();
    }

    /*
     * Test "help" command and check if the help overlay correctly displays for
     * the user.
     */
    public static void testHelpTaskDone() {
        main.execute("help");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(true, main.helpContent.isVisible());
    }
}
