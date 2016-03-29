package jfdi.test.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.swing.ImageIcon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import com.google.common.io.Files;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import jfdi.storage.apis.MainStorage;
import jfdi.ui.AutoCompleteTextField;
import jfdi.ui.Constants;
import jfdi.ui.IUserInterface;
import jfdi.ui.MainController;
import jfdi.ui.MainSetUp;
import jfdi.ui.UI;
import jfdi.ui.items.HelpItem;
import jfdi.ui.items.ListItem;

public class TestMainSetUp extends ApplicationTest {

    /* The widgets of the GUI used for the tests. */
    Rectangle incompleteBox;
    Rectangle overdueBox;
    Rectangle upcomingBox;
    Rectangle allBox;
    Rectangle completedBox;
    Rectangle searchBox;
    Rectangle surpriseBox;
    Rectangle helpBox;
    Label incompleteTab;
    Label overdueTab;
    Label upcomingTab;
    Label allTab;
    Label completedTab;
    Label searchTab;
    Label surpriseTab;
    Label helpTab;
    Label incompleteCount;
    Label overdueCount;
    Label upcomingCount;
    Label dayDisplayer;
    ListView<ListItem> listMain;
    TextArea fbArea;
    AutoCompleteTextField cmdArea;
    ListView<HelpItem> helpContent;
    VBox surpriseOverlay;
    Label surpriseTitle;
    Label taskDesc;
    Label taskTime;
    Label surpriseBottom;
    ImageView noSurpriseOverlay;

    Stage stage;
    Scene scene;
    Parent rootLayout;
    AnchorPane listLayout;
    MainController controller;

    /* The original storage path - to revert after the tests */
    private String originalStorageDirectory = null;

    /* This operation comes from ApplicationTest and loads the GUI to test. */
    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;
        this.stage.setTitle(Constants.PRODUCT_NAME);
        
        setLogo();
        loadFonts();
        initRootLayout();
        initView();
        stage.show();
        /* To ensure keyboard entry is on JFDI display*/
        stage.toFront();

    }

    private void setLogo() {
        stage.getIcons().add(new Image(Constants.URL_LOGO_PATH));

        // Set Icon for OSX
        // Need to use Apple Java Extension, using reflection to load the class so that JFDI is compilable
        if (System.getProperty("os.name").startsWith("Mac OS")) {
            try {
                Class util = Class.forName("com.apple.eawt.Application");
                Method getApplication = util.getMethod("getApplication", new Class[0]);
                Object application = getApplication.invoke(util);
                Class[] params = new Class[1];
                params[0] = java.awt.Image.class;
                Method setDockIconImage = util.getMethod("setDockIconImage", params);
                setDockIconImage.invoke(application,
                    new ImageIcon(UI.class.getResource(Constants.URL_LOGO_PATH)).getImage());
            } catch (Exception e) {
                System.out.println("Not OS X");
            }
        }
    }

    private void loadFonts() {
        
        Font.loadFont(
                MainSetUp.class.getResource("/ui/fonts/HammersmithOne.ttf")
                    .toExternalForm(), 12);
            Font.loadFont(
                MainSetUp.class.getResource("/ui/fonts/TitilliumWeb-Light.ttf")
                    .toExternalForm(), 24);
            Font.loadFont(
                MainSetUp.class.getResource("/ui/fonts/Lucida Console.ttf")
                    .toExternalForm(), 24);
    }

    private void initRootLayout() throws IOException {

        rootLayout = FXMLLoader.load(getClass().getResource(Constants.URL_ROOT_PATH));
        Scene scene = new Scene(rootLayout);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.setResizable(false);
    }

    private void initView() throws Exception {

        IUserInterface ui = UI.getInstance();

        // Load View
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(Constants.URL_LIST_PATH));
        listLayout = (AnchorPane) loader.load();

        // Initialize Controller
        controller = loader.getController();

        // Link UI with Controller
        ui.setController(controller);
        ui.init();

        ((BorderPane) rootLayout).setCenter(listLayout);
        controller.setStage(stage);

        // Link Controller with UI, MainSetUp and CommandHandler
        controller.setUi(ui);
        controller.initNotiBubbles();

        //controller.importantList.removeAll(controller.importantList);

        File tempDir = Files.createTempDir();
        originalStorageDirectory = MainStorage.getInstance().getCurrentDirectory();
        MainStorage.getInstance().use(tempDir.getAbsolutePath());

        controller.hideOverlays();
        controller.displayList(Constants.CTRL_CMD_OVERDUE);
        controller.displayList(Constants.CTRL_CMD_UPCOMING);
        controller.displayList(Constants.CTRL_CMD_INCOMPLETE);
        initThread();
        ui.displayWelcome();
    }

    private void initThread() {
        
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException {
                while (true) {
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            controller.updateNotiBubbles();
                        }
                    });
                }
            }
        };

        controller.incompleteCount.textProperty().bind(
            controller.incompletePlaceHdr);
        controller.overdueCount.textProperty().bind(controller.overduePlaceHdr);
        controller.upcomingCount.textProperty().bind(
            controller.upcomingPlaceHdr);

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    /* Just a shortcut to retrieve widgets in the GUI. */
    public <T extends Node> T find(final String query) {
        /** TestFX provides many operations to retrieve elements from the loaded GUI. */
        return lookup(query).queryFirst();
    }

    @Before
    public void setUp() {
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

    /* To clear the ongoing events */
    @After
    public void tearDown() throws Exception {
        /* Close the window. It will be re-opened at the next test. */
        FxToolkit.hideStage();
        release(new KeyCode[] {});
        release(new MouseButton[] {});
        MainStorage.getInstance().use(originalStorageDirectory);
    }

    @Test
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

    /*
     * Test a simple "add hello" command and check if the feedback displayed matches the expected lines.
     */
    @Test
    public void testAddTask() {
        clickOn(cmdArea).type(KeyCode.BACK_SPACE).type(KeyCode.A).type(KeyCode.D).type(KeyCode.D).type(KeyCode.SPACE)
        .type(KeyCode.H).type(KeyCode.E).type(KeyCode.L).type(KeyCode.L).type(KeyCode.O).type(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("The feedback message does not match the intended result.",
                fbArea.getText(), "J.F.D.I. : " + String.format(
                        Constants.CMD_SUCCESS_ADDED, "hello"));
    }
}
