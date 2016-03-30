package jfdi.test.ui;

import static org.junit.Assume.assumeTrue;

import java.awt.Robot;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.loadui.testfx.FXScreenController;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.FXTestUtils;
import org.loadui.testfx.utils.UserInputDetector;

import com.google.common.io.Files;
import com.google.common.util.concurrent.SettableFuture;

import javafx.scene.Parent;
import javafx.stage.Stage;
import jfdi.common.utilities.JfdiLogger;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.exceptions.FilesReplacedException;
import jfdi.storage.exceptions.InvalidFilePathException;
import jfdi.ui.MainSetUp;

public class UITest extends GuiTest {

    protected static final SettableFuture<Stage> STAGE_FUTURE = SettableFuture.create();
    private static final Logger logger = JfdiLogger.getLogger();


    private final Robot robot;
    private final FXScreenController controller;

    /* The original storage path - to revert after the tests */
    private String originalStorageDirectory = null;

    protected static class TestMainSetUp extends MainSetUp {
        public TestMainSetUp() {
            super();
        }

        @Override
        public void start(Stage primaryStage) throws Exception {
            super.start(primaryStage);
            STAGE_FUTURE.set(primaryStage);

        }
    }

    public UITest() {
        super();
        this.controller = getController();
        this.robot = getRobot();
    }

    @Before
    @Override
    public void setupStage() throws Throwable {
        assumeTrue(!UserInputDetector.instance.hasDetectedUserInput());

        createTempStorageDir();
        beforeStageStarts();

        if (stage == null) {
            FXTestUtils.launchApp(TestMainSetUp.class);
        }
        try {
            stage = targetWindow(STAGE_FUTURE.get(25, TimeUnit.SECONDS));
            FXTestUtils.bringToFront(stage);
        } catch (Exception e) {
            throw new RuntimeException("Unable to show stage", e);
        }
    }

    private void beforeStageStarts() {
        // method to be overridden if anything needs to be done (e.g. to the
        // json) before the stage starts
    }

    private void createTempStorageDir() {
        File tempDir = Files.createTempDir();
        originalStorageDirectory = MainStorage.getInstance().getCurrentDirectory();
        try {
            MainStorage.getInstance().use(tempDir.getAbsolutePath());
        } catch (InvalidFilePathException | FilesReplacedException e) {
            logger.fine(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected Parent getRootNode() {
        return stage.getScene().getRoot();
    }

    private FXScreenController getController() {
        try {
            return (FXScreenController) FieldUtils.readField(this, "controller", true);
        } catch (IllegalAccessException e) {
            logger.fine(e.getLocalizedMessage());
            return null;
        }
    }

    private Robot getRobot() {
        try {
            return (Robot) FieldUtils.readField(controller, "robot", true);
        } catch (IllegalAccessException e) {
            logger.fine(e.getLocalizedMessage());
            return null;
        }
    }

}
