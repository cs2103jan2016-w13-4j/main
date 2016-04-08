package jfdi.test.ui;

import java.io.File;

import org.testfx.util.WaitForAsyncUtils;

import com.google.common.io.Files;

import javafx.application.Platform;
import jfdi.logic.ControlCenter;
import jfdi.storage.apis.MainStorage;

public abstract class UiTest {

    protected TestMain main;
    protected String testDir;
    protected String originalDir = MainStorage.getInstance().getCurrentDirectory();

    UiTest(TestMain main) {
        this.main = main;
    }

    public void init() {
        File tempDir = Files.createTempDir();
        Platform.runLater(() -> ControlCenter.getInstance().handleInput(
                String.format("use %s", tempDir.getAbsolutePath())));
        WaitForAsyncUtils.waitForFxEvents();
        testDir = tempDir.getAbsolutePath();
    }

    public void done() {
        Platform.runLater(() -> ControlCenter.getInstance().handleInput(String.format("use %s", originalDir)));
        WaitForAsyncUtils.waitForFxEvents();
    }

    abstract void run();

}
