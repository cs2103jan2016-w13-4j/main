//@@author A0129538W

package jfdi.test.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        assertTrue(main.helpContent.isVisible());
    }

    /*
     * Test another command after "help" and check if the help overlay correctly
     * hides in the display.
     */
    public void testAfterHelp() {
        main.execute("help");
        main.execute("list");
        WaitForAsyncUtils.waitForFxEvents();
        assertFalse(main.helpContent.isVisible());
    }

}
