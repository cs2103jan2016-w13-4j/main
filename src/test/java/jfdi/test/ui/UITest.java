package jfdi.test.ui;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.loadui.testfx.FXScreenController;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.FXTestUtils;
import org.loadui.testfx.utils.UserInputDetector;
import org.testfx.api.FxToolkit;

import com.google.common.io.Files;
import com.google.common.util.concurrent.SettableFuture;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import jfdi.common.utilities.JfdiLogger;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.exceptions.FilesReplacedException;
import jfdi.storage.exceptions.InvalidFilePathException;
import jfdi.ui.AutoCompleteTextField;
import jfdi.ui.MainSetUp;
import jfdi.ui.items.HelpItem;
import jfdi.ui.items.ListItem;

public class UITest extends GuiTest {
    
    /* The widgets of the GUI used for the tests. */
    protected Rectangle incompleteBox;
    protected Rectangle overdueBox;
    protected Rectangle upcomingBox;
    protected Rectangle allBox;
    protected Rectangle completedBox;
    protected Rectangle searchBox;
    protected Rectangle surpriseBox;
    protected Rectangle helpBox;
    protected Label incompleteTab;
    protected Label overdueTab;
    protected Label upcomingTab;
    protected Label allTab;
    protected Label completedTab;
    protected Label searchTab;
    protected Label surpriseTab;
    protected Label helpTab;
    protected Label incompleteCount;
    protected Label overdueCount;
    protected Label upcomingCount;
    protected Label dayDisplayer;
    protected ListView<ListItem> listMain;
    protected TextArea fbArea;
    protected AutoCompleteTextField cmdArea;
    protected ListView<HelpItem> helpContent;
    protected VBox surpriseOverlay;
    protected Label surpriseTitle;
    protected Label taskDesc;
    protected Label taskTime;
    protected Label surpriseBottom;
    protected ImageView noSurpriseOverlay;

    protected static final SettableFuture<Stage> STAGE_FUTURE = SettableFuture.create();
    private static final Logger logger = JfdiLogger.getLogger();


    //private final Robot robot;
    //private final FXScreenController controller;

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
        //this.controller = getController();
        //this.robot = getRobot();
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
            //setUp();
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

/*    private Robot getRobot() {
        try {
            return (Robot) FieldUtils.readField(controller, "robot", true);
        } catch (IllegalAccessException e) {
            logger.fine(e.getLocalizedMessage());
            return null;
        }
    }*/
    
    /* Just a shortcut to retrieve widgets in the GUI. */
    //public <T extends Node> T find(final String query) {
        /**
         * TestFX provides many operations to retrieve elements from the loaded
         * GUI.
         */
        //return lookup(query).queryFirst();
    //}
    
    public void setUp() throws Exception {

        FxToolkit.registerPrimaryStage();
        /* Retrieve the tested widgets from the GUI. */
        incompleteBox = find("#incompleteBox");
        overdueBox = find("#overdueBox");
        upcomingBox = find("#upcomingBox");
        allBox = find("#allBox");
        completedBox = find("#completedBox");
        searchBox = find("#searchBox");
        surpriseBox = find("#surpriseBox");
        helpBox = find("#helpBox");
        incompleteTab = find("#incompleteTab");
        overdueTab = find("#overdueTab");
        upcomingTab = find("#upcomingTab");
        allTab = find("#allTab");
        completedTab = find("#completedTab");
        searchTab = find("#searchTab");
        surpriseTab = find("#surpriseTab");
        helpTab = find("#helpTab");
        incompleteCount = find("#incompleteCount");
        overdueCount = find("#overdueCount");
        upcomingCount = find("#upcomingCount");
        dayDisplayer = find("#dayDisplayer");
        listMain = find("#listMain");
        fbArea = find("#fbArea");
        cmdArea = find("#cmdArea");
        helpContent = find("#helpContent");
        surpriseOverlay = find("#surpriseOverlay");
        surpriseTitle = find("#surpriseTitle");
        taskDesc = find("#taskDesc");
        taskTime = find("#taskTime");
        surpriseBottom = find("#surpriseBottom");
        noSurpriseOverlay = find("#noSurpriseOverlay");
    }
    
    //@Test
    public void testWidgetsExist() {

        final String errMsg = " %s cannot be retrieved!";

        assertNotNull(String.format(errMsg, "incompleteBox"), incompleteBox);
        assertNotNull(String.format(errMsg, "overdueBox"), overdueBox);
        assertNotNull(String.format(errMsg, "upcomingBox"), upcomingBox);
        assertNotNull(String.format(errMsg, "allBox"), allBox);
        assertNotNull(String.format(errMsg, "completedBox"), completedBox);
        assertNotNull(String.format(errMsg, "searchBox"), searchBox);
        assertNotNull(String.format(errMsg, "surpriseBox"), surpriseBox);
        assertNotNull(String.format(errMsg, "helpBox"), helpBox);
        assertNotNull(String.format(errMsg, "incompleteTab"), incompleteTab);
        assertNotNull(String.format(errMsg, "overdueTab"), overdueTab);
        assertNotNull(String.format(errMsg, "upcomingTab"), upcomingTab);
        assertNotNull(String.format(errMsg, "allTab"), allTab);
        assertNotNull(String.format(errMsg, "completedTab"), completedTab);
        assertNotNull(String.format(errMsg, "searchTab"), searchTab);
        assertNotNull(String.format(errMsg, "surpriseTab"), surpriseTab);
        assertNotNull(String.format(errMsg, "helpTab"), helpTab);
        assertNotNull(String.format(errMsg, "incompleteCount"), incompleteCount);
        assertNotNull(String.format(errMsg, "overdueCount"), overdueCount);
        assertNotNull(String.format(errMsg, "upcomingCount"), upcomingCount);
        assertNotNull(String.format(errMsg, "dayDisplayer"), dayDisplayer);
        assertNotNull(String.format(errMsg, "listMain"), listMain);
        assertNotNull(String.format(errMsg, "fbArea"), fbArea);
        assertNotNull(String.format(errMsg, "cmdArea"), cmdArea);
        assertNotNull(String.format(errMsg, "helpContent"), helpContent);
        assertNotNull(String.format(errMsg, "surpriseOverlay"), surpriseOverlay);
        assertNotNull(String.format(errMsg, "surpriseTitle"), surpriseTitle);
        assertNotNull(String.format(errMsg, "taskDesc"), taskDesc);
        assertNotNull(String.format(errMsg, "taskTime"), taskTime);
        assertNotNull(String.format(errMsg, "surpriseBottom"), surpriseBottom);
        assertNotNull(String.format(errMsg, "noSurpriseOverlay"), noSurpriseOverlay);
    }

}
