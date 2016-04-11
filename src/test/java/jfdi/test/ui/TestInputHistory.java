//@@author A0129538W

package jfdi.test.ui;

import static org.junit.Assert.assertEquals;

import javafx.scene.input.KeyCode;

public class TestInputHistory extends UiTest {

    TestInputHistory(TestMain main) {
        super(main);
    }

    @Override
    void run() {
        testInputHistory();
    }

    private void testInputHistory() {
        String cmd1 = "Command 1";
        String cmd2 = "Command 2";
        main.execute(cmd1);
        main.execute(cmd2);

        main.type(KeyCode.UP);
        assertEquals(cmd2, main.cmdArea.getText());

        main.type(KeyCode.UP);
        assertEquals(cmd1, main.cmdArea.getText());

        main.type(KeyCode.DOWN);
        assertEquals(cmd2, main.cmdArea.getText());

        main.cmdArea.clear();
    }

}
