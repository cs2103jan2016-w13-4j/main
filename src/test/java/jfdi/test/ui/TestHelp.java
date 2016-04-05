package jfdi.test.ui;

import static org.junit.Assert.assertEquals;

import org.testfx.util.WaitForAsyncUtils;

public class TestHelp extends UiTest {

    TestHelp(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        testHelpTaskDone();
    }

    /*
     * Test "help" command and check if the help overlay correctly displays for
     * the user.
     */
    public void testHelpTaskDone() {
        main.execute("help");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(true, main.helpContent.isVisible());
    }
}
