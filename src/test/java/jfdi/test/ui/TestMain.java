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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

public class TestMain extends ApplicationTest {

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
        /* To ensure keyboard entry is on JFDI display */
        stage.toFront();

    }

    private void setLogo() {
        stage.getIcons().add(new Image(Constants.URL_LOGO_PATH));

        // Set Icon for OSX
        // Need to use Apple Java Extension, using reflection to load the class
        // so that JFDI is compilable
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

        Font.loadFont(MainSetUp.class.getResource("/ui/fonts/HammersmithOne.ttf").toExternalForm(), 12);
        Font.loadFont(MainSetUp.class.getResource("/ui/fonts/TitilliumWeb-Light.ttf").toExternalForm(), 24);
        Font.loadFont(MainSetUp.class.getResource("/ui/fonts/Lucida Console.ttf").toExternalForm(), 24);
    }

    private void initRootLayout() throws IOException {

        rootLayout = FXMLLoader.load(UI.class.getResource(Constants.URL_ROOT_PATH));
        Scene scene = new Scene(rootLayout);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.setResizable(false);
    }

    private void initView() throws Exception {

        IUserInterface ui = UI.getInstance();

        // Load View
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(UI.class.getResource(Constants.URL_LIST_PATH));
        listLayout = (AnchorPane) loader.load();

        // Initialize Controller
        UI.getInstance().controller = loader.getController();
        controller = UI.getInstance().controller;
        ui.init();

        ((BorderPane) rootLayout).setCenter(listLayout);
        controller.setStage(stage);

        // Link Controller with UI, MainSetUp and CommandHandler
        controller.setUi(ui);

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

        controller.incompleteCount.textProperty().bind(controller.incompletePlaceHdr);
        controller.overdueCount.textProperty().bind(controller.overduePlaceHdr);
        controller.upcomingCount.textProperty().bind(controller.upcomingPlaceHdr);

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    /* Just a shortcut to retrieve widgets in the GUI. */
    public <T extends Node> T find(final String query) {
        /**
         * TestFX provides many operations to retrieve elements from the loaded
         * GUI.
         */
        return lookup(query).query();
    }

    @Before
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
//        surpriseOverlay = find("#surpriseOverlay");
        surpriseTitle = find("#surpriseTitle");
        taskDesc = find("#taskDesc");
        taskTime = find("#taskTime");
        surpriseBottom = find("#surpriseBottom");
//        noSurpriseOverlay = find("#noSurpriseOverlay");
    }

    /* To clear the ongoing events */
    @After
    public void tearDown() throws Exception {
        /* Close the window. It will be re-opened at the next test. */
        FxToolkit.cleanupStages();
        FxToolkit.hideStage();
        release(new KeyCode[] {});
        release(new MouseButton[] {});
        MainStorage.getInstance().use(originalStorageDirectory);
    }

    @Test
    public void uiTests() {
        testWidgetsExist();
        testAddTaskDone();
        testAddDuplicateTask();
        testAddEmptyTask();
        testHelpTaskDone();
    }

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
//        assertNotNull(String.format(errMsg, "surpriseOverlay"), surpriseOverlay);
        assertNotNull(String.format(errMsg, "surpriseTitle"), surpriseTitle);
        assertNotNull(String.format(errMsg, "taskDesc"), taskDesc);
        assertNotNull(String.format(errMsg, "taskTime"), taskTime);
        assertNotNull(String.format(errMsg, "surpriseBottom"), surpriseBottom);
//        assertNotNull(String.format(errMsg, "noSurpriseOverlay"), noSurpriseOverlay);
    }

    /*
     * Test a simple "add testing1" command and check if the task is added to
     * the incomplete list as an item, and check if the feedback displayed
     * matches the expected lines.
     */
    public void testAddTaskDone() {
        String taskName = "testing1";
        int listSize = controller.importantList.size();
        StringProperty notiSize = new SimpleStringProperty(Integer.toString(listSize + 1));
        String expectedFeedback = String.format(Constants.UI_MESSAGE_RESPONSE,
                String.format(Constants.CMD_SUCCESS_ADDED, taskName)).trim();

        addTask(taskName);

        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(expectedFeedback, fbArea.getText());
        assertEquals(listSize + 1, controller.importantList.size());
        assertEquals(notiSize.getValue(), controller.incompletePlaceHdr.getValue());
    }

    /*
     * Test the "add testing1" command again to check if error for duplicated
     * task can be detected and reflected correctly
     */
    public void testAddDuplicateTask() {
        int listSize = controller.importantList.size();
        StringProperty notiSize = new SimpleStringProperty(Integer.toString(listSize));
        String expectedFeedback = String.format(Constants.UI_MESSAGE_ERROR, Constants.CMD_ERROR_CANT_ADD_DUPLICATE).trim();

        String taskName = "testing1";
        addTask(taskName);

        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(expectedFeedback, fbArea.getText());
        assertEquals(listSize, controller.importantList.size());
        assertEquals(notiSize.getValue(), controller.incompletePlaceHdr.getValue());
    }

    /*
     * Test an "add " command with empty description and check if the error is
     * correctly detected and reflected.
     */
    public void testAddEmptyTask() {
        int listSize = controller.importantList.size();
        StringProperty notiSize = new SimpleStringProperty(Integer.toString(listSize));

        addTask("");

        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(Constants.CMD_ERROR_CANT_ADD_EMPTY, fbArea.getText());
        assertEquals(listSize, controller.importantList.size());
        assertEquals(notiSize, controller.incompletePlaceHdr);
    }

    /*
     * Test "help" command and check if the help overlay correctly displays for
     * the user.
     */
    public void testHelpTaskDone() {
        type("help\n");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(true, helpContent.isVisible());
    }

    /*
     * Helper methods
     */

    private void addTask(String taskName) {
        execute(String.format("add %s", taskName));
    }

    private void execute(String command) {
        type(command + '\n');
    }

    private void type(String command) {
        for (char c : command.toCharArray()) {
            type(c);
        }
    }

    private void type(char c) {
        switch (c) {
        case 'a': type(KeyCode.A); break;
        case 'b': type(KeyCode.B); break;
        case 'c': type(KeyCode.C); break;
        case 'd': type(KeyCode.D); break;
        case 'e': type(KeyCode.E); break;
        case 'f': type(KeyCode.F); break;
        case 'g': type(KeyCode.G); break;
        case 'h': type(KeyCode.H); break;
        case 'i': type(KeyCode.I); break;
        case 'j': type(KeyCode.J); break;
        case 'k': type(KeyCode.K); break;
        case 'l': type(KeyCode.L); break;
        case 'm': type(KeyCode.M); break;
        case 'n': type(KeyCode.N); break;
        case 'o': type(KeyCode.O); break;
        case 'p': type(KeyCode.P); break;
        case 'q': type(KeyCode.Q); break;
        case 'r': type(KeyCode.R); break;
        case 's': type(KeyCode.S); break;
        case 't': type(KeyCode.T); break;
        case 'u': type(KeyCode.U); break;
        case 'v': type(KeyCode.V); break;
        case 'w': type(KeyCode.W); break;
        case 'x': type(KeyCode.X); break;
        case 'y': type(KeyCode.Y); break;
        case 'z': type(KeyCode.Z); break;
        case 'A': type(KeyCode.SHIFT, KeyCode.A); break;
        case 'B': type(KeyCode.SHIFT, KeyCode.B); break;
        case 'C': type(KeyCode.SHIFT, KeyCode.C); break;
        case 'D': type(KeyCode.SHIFT, KeyCode.D); break;
        case 'E': type(KeyCode.SHIFT, KeyCode.E); break;
        case 'F': type(KeyCode.SHIFT, KeyCode.F); break;
        case 'G': type(KeyCode.SHIFT, KeyCode.G); break;
        case 'H': type(KeyCode.SHIFT, KeyCode.H); break;
        case 'I': type(KeyCode.SHIFT, KeyCode.I); break;
        case 'J': type(KeyCode.SHIFT, KeyCode.J); break;
        case 'K': type(KeyCode.SHIFT, KeyCode.K); break;
        case 'L': type(KeyCode.SHIFT, KeyCode.L); break;
        case 'M': type(KeyCode.SHIFT, KeyCode.M); break;
        case 'N': type(KeyCode.SHIFT, KeyCode.N); break;
        case 'O': type(KeyCode.SHIFT, KeyCode.O); break;
        case 'P': type(KeyCode.SHIFT, KeyCode.P); break;
        case 'Q': type(KeyCode.SHIFT, KeyCode.Q); break;
        case 'R': type(KeyCode.SHIFT, KeyCode.R); break;
        case 'S': type(KeyCode.SHIFT, KeyCode.S); break;
        case 'T': type(KeyCode.SHIFT, KeyCode.T); break;
        case 'U': type(KeyCode.SHIFT, KeyCode.U); break;
        case 'V': type(KeyCode.SHIFT, KeyCode.V); break;
        case 'W': type(KeyCode.SHIFT, KeyCode.W); break;
        case 'X': type(KeyCode.SHIFT, KeyCode.X); break;
        case 'Y': type(KeyCode.SHIFT, KeyCode.Y); break;
        case 'Z': type(KeyCode.SHIFT, KeyCode.Z); break;
        case '`': type(KeyCode.BACK_QUOTE); break;
        case '0': type(KeyCode.DIGIT0); break;
        case '1': type(KeyCode.DIGIT1); break;
        case '2': type(KeyCode.DIGIT2); break;
        case '3': type(KeyCode.DIGIT3); break;
        case '4': type(KeyCode.DIGIT4); break;
        case '5': type(KeyCode.DIGIT5); break;
        case '6': type(KeyCode.DIGIT6); break;
        case '7': type(KeyCode.DIGIT7); break;
        case '8': type(KeyCode.DIGIT8); break;
        case '9': type(KeyCode.DIGIT9); break;
        case '-': type(KeyCode.MINUS); break;
        case '=': type(KeyCode.EQUALS); break;
        case '~': type(KeyCode.SHIFT, KeyCode.BACK_QUOTE); break;
        case '!': type(KeyCode.SHIFT, KeyCode.DIGIT1); break;
        case '@': type(KeyCode.SHIFT, KeyCode.DIGIT2); break;
        case '#': type(KeyCode.SHIFT, KeyCode.DIGIT3); break;
        case '$': type(KeyCode.SHIFT, KeyCode.DIGIT4); break;
        case '%': type(KeyCode.SHIFT, KeyCode.DIGIT5); break;
        case '^': type(KeyCode.SHIFT, KeyCode.DIGIT6); break;
        case '&': type(KeyCode.SHIFT, KeyCode.DIGIT7); break;
        case '*': type(KeyCode.SHIFT, KeyCode.DIGIT8); break;
        case '(': type(KeyCode.SHIFT, KeyCode.DIGIT9); break;
        case ')': type(KeyCode.SHIFT, KeyCode.DIGIT0); break;
        case '_': type(KeyCode.SHIFT, KeyCode.MINUS); break;
        case '+': type(KeyCode.SHIFT, KeyCode.EQUALS); break;
        case '\b': type(KeyCode.BACK_SPACE); break;
        case '\t': type(KeyCode.TAB); break;
        case '\n': type(KeyCode.ENTER); break;
        case '[': type(KeyCode.OPEN_BRACKET); break;
        case ']': type(KeyCode.CLOSE_BRACKET); break;
        case '\\': type(KeyCode.BACK_SLASH); break;
        case '{': type(KeyCode.SHIFT, KeyCode.OPEN_BRACKET); break;
        case '}': type(KeyCode.SHIFT, KeyCode.CLOSE_BRACKET); break;
        case '|': type(KeyCode.SHIFT, KeyCode.BACK_SLASH); break;
        case ';': type(KeyCode.SEMICOLON); break;
        case ':': type(KeyCode.SHIFT, KeyCode.SEMICOLON); break;
        case '\'': type(KeyCode.QUOTE); break;
        case '"': type(KeyCode.SHIFT, KeyCode.QUOTE); break;
        case ',': type(KeyCode.COMMA); break;
        case '<': type(KeyCode.SHIFT, KeyCode.COMMA); break;
        case '.': type(KeyCode.PERIOD); break;
        case '>': type(KeyCode.SHIFT, KeyCode.PERIOD); break;
        case '/': type(KeyCode.SLASH); break;
        case '?': type(KeyCode.SHIFT, KeyCode.SLASH); break;
        case ' ': type(KeyCode.SPACE); break;
        default:
            throw new IllegalArgumentException("Cannot type character " + c);
        }
    }
}
