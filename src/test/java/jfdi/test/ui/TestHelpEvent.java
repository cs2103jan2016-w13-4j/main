package jfdi.test.ui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

import javafx.scene.input.KeyCode;

public class TestHelpEvent extends TestMain {
    
    /*
     * Test "help" command and check if the help overlay correctly displays for the user.
     */
    @Test
    public void tesHelpTaskDone() {

        clickOn(cmdArea).type(KeyCode.BACK_SPACE).type(KeyCode.H).type(KeyCode.E).type(KeyCode.L).type(KeyCode.P)
                .type(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("The help overlay is not properly shown.", true, helpContent.isVisible());
    }
}
