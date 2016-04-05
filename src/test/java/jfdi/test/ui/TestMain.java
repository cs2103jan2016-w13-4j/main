package jfdi.test.ui;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.swing.ImageIcon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

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
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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
    AnchorPane surpriseOverlay;
    Label surpriseTitle;
    Label taskDesc;
    Label taskTime;
    Label surpriseBottom;
    AnchorPane noSurpriseOverlay;

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
        FxToolkit.cleanupStages();
        FxToolkit.hideStage();
        release(new KeyCode[] {});
        release(new MouseButton[] {});
        MainStorage.getInstance().use(originalStorageDirectory);
    }

    @Test
    public void uiTests() {
        UiTest[] tests = {
            new TestWidgets(this),
            new TestAdd(this),
            new TestMark(this),
            new TestHelp(this)
        };
        for (UiTest test : tests) {
            test.run();
            test.done();
        }
    }

    /*
     * Helper methods
     */

    public void addTask(String taskName) {
        execute(String.format("add %s", taskName));
    }

    public void addRandomTasks(int num) {
        for (int i = 0; i < num; i++) {
            addTask(String.valueOf(i));
        }
    }

    public void execute(String command) {
        type(command + '\n');
    }

    public void assertErrorMessage(String message) {
        assertFeedbackMessage(String.format(Constants.UI_MESSAGE_ERROR, message));
    }

    public void assertResponseMessage(String message) {
        assertFeedbackMessage(String.format(Constants.UI_MESSAGE_RESPONSE, message));
    }

    public void assertFeedbackMessage(String message) {
        assertEquals(message.trim(), fbArea.getText());
    }

    private void type(String command) {
        for (char c : command.toCharArray()) {
            type(c);
        }
    }

    private void type(char c) {
        switch (c) {
            case 'a':
                doType(KeyCode.A);
                break;
            case 'b':
                doType(KeyCode.B);
                break;
            case 'c':
                doType(KeyCode.C);
                break;
            case 'd':
                doType(KeyCode.D);
                break;
            case 'e':
                doType(KeyCode.E);
                break;
            case 'f':
                doType(KeyCode.F);
                break;
            case 'g':
                doType(KeyCode.G);
                break;
            case 'h':
                doType(KeyCode.H);
                break;
            case 'i':
                doType(KeyCode.I);
                break;
            case 'j':
                doType(KeyCode.J);
                break;
            case 'k':
                doType(KeyCode.K);
                break;
            case 'l':
                doType(KeyCode.L);
                break;
            case 'm':
                doType(KeyCode.M);
                break;
            case 'n':
                doType(KeyCode.N);
                break;
            case 'o':
                doType(KeyCode.O);
                break;
            case 'p':
                doType(KeyCode.P);
                break;
            case 'q':
                doType(KeyCode.Q);
                break;
            case 'r':
                doType(KeyCode.R);
                break;
            case 's':
                doType(KeyCode.S);
                break;
            case 't':
                doType(KeyCode.T);
                break;
            case 'u':
                doType(KeyCode.U);
                break;
            case 'v':
                doType(KeyCode.V);
                break;
            case 'w':
                doType(KeyCode.W);
                break;
            case 'x':
                doType(KeyCode.X);
                break;
            case 'y':
                doType(KeyCode.Y);
                break;
            case 'z':
                doType(KeyCode.Z);
                break;
            case 'A':
                doType(KeyCode.SHIFT, KeyCode.A);
                break;
            case 'B':
                doType(KeyCode.SHIFT, KeyCode.B);
                break;
            case 'C':
                doType(KeyCode.SHIFT, KeyCode.C);
                break;
            case 'D':
                doType(KeyCode.SHIFT, KeyCode.D);
                break;
            case 'E':
                doType(KeyCode.SHIFT, KeyCode.E);
                break;
            case 'F':
                doType(KeyCode.SHIFT, KeyCode.F);
                break;
            case 'G':
                doType(KeyCode.SHIFT, KeyCode.G);
                break;
            case 'H':
                doType(KeyCode.SHIFT, KeyCode.H);
                break;
            case 'I':
                doType(KeyCode.SHIFT, KeyCode.I);
                break;
            case 'J':
                doType(KeyCode.SHIFT, KeyCode.J);
                break;
            case 'K':
                doType(KeyCode.SHIFT, KeyCode.K);
                break;
            case 'L':
                doType(KeyCode.SHIFT, KeyCode.L);
                break;
            case 'M':
                doType(KeyCode.SHIFT, KeyCode.M);
                break;
            case 'N':
                doType(KeyCode.SHIFT, KeyCode.N);
                break;
            case 'O':
                doType(KeyCode.SHIFT, KeyCode.O);
                break;
            case 'P':
                doType(KeyCode.SHIFT, KeyCode.P);
                break;
            case 'Q':
                doType(KeyCode.SHIFT, KeyCode.Q);
                break;
            case 'R':
                doType(KeyCode.SHIFT, KeyCode.R);
                break;
            case 'S':
                doType(KeyCode.SHIFT, KeyCode.S);
                break;
            case 'T':
                doType(KeyCode.SHIFT, KeyCode.T);
                break;
            case 'U':
                doType(KeyCode.SHIFT, KeyCode.U);
                break;
            case 'V':
                doType(KeyCode.SHIFT, KeyCode.V);
                break;
            case 'W':
                doType(KeyCode.SHIFT, KeyCode.W);
                break;
            case 'X':
                doType(KeyCode.SHIFT, KeyCode.X);
                break;
            case 'Y':
                doType(KeyCode.SHIFT, KeyCode.Y);
                break;
            case 'Z':
                doType(KeyCode.SHIFT, KeyCode.Z);
                break;
            case '`':
                doType(KeyCode.BACK_QUOTE);
                break;
            case '0':
                doType(KeyCode.DIGIT0);
                break;
            case '1':
                doType(KeyCode.DIGIT1);
                break;
            case '2':
                doType(KeyCode.DIGIT2);
                break;
            case '3':
                doType(KeyCode.DIGIT3);
                break;
            case '4':
                doType(KeyCode.DIGIT4);
                break;
            case '5':
                doType(KeyCode.DIGIT5);
                break;
            case '6':
                doType(KeyCode.DIGIT6);
                break;
            case '7':
                doType(KeyCode.DIGIT7);
                break;
            case '8':
                doType(KeyCode.DIGIT8);
                break;
            case '9':
                doType(KeyCode.DIGIT9);
                break;
            case '-':
                doType(KeyCode.MINUS);
                break;
            case '=':
                doType(KeyCode.EQUALS);
                break;
            case '~':
                doType(KeyCode.SHIFT, KeyCode.BACK_QUOTE);
                break;
            case '!':
                doType(KeyCode.SHIFT, KeyCode.DIGIT1);
                break;
            case '@':
                doType(KeyCode.SHIFT, KeyCode.DIGIT2);
                break;
            case '#':
                doType(KeyCode.SHIFT, KeyCode.DIGIT3);
                break;
            case '$':
                doType(KeyCode.SHIFT, KeyCode.DIGIT4);
                break;
            case '%':
                doType(KeyCode.SHIFT, KeyCode.DIGIT5);
                break;
            case '^':
                doType(KeyCode.SHIFT, KeyCode.DIGIT6);
                break;
            case '&':
                doType(KeyCode.SHIFT, KeyCode.DIGIT7);
                break;
            case '*':
                doType(KeyCode.SHIFT, KeyCode.DIGIT8);
                break;
            case '(':
                doType(KeyCode.SHIFT, KeyCode.DIGIT9);
                break;
            case ')':
                doType(KeyCode.SHIFT, KeyCode.DIGIT0);
                break;
            case '_':
                doType(KeyCode.SHIFT, KeyCode.MINUS);
                break;
            case '+':
                doType(KeyCode.SHIFT, KeyCode.EQUALS);
                break;
            case '\b':
                doType(KeyCode.BACK_SPACE);
                break;
            case '\t':
                doType(KeyCode.TAB);
                break;
            case '\n':
                doType(KeyCode.ENTER);
                break;
            case '[':
                doType(KeyCode.OPEN_BRACKET);
                break;
            case ']':
                doType(KeyCode.CLOSE_BRACKET);
                break;
            case '\\':
                doType(KeyCode.BACK_SLASH);
                break;
            case '{':
                doType(KeyCode.SHIFT, KeyCode.OPEN_BRACKET);
                break;
            case '}':
                doType(KeyCode.SHIFT, KeyCode.CLOSE_BRACKET);
                break;
            case '|':
                doType(KeyCode.SHIFT, KeyCode.BACK_SLASH);
                break;
            case ';':
                doType(KeyCode.SEMICOLON);
                break;
            case ':':
                doType(KeyCode.SHIFT, KeyCode.SEMICOLON);
                break;
            case '\'':
                doType(KeyCode.QUOTE);
                break;
            case '"':
                doType(KeyCode.SHIFT, KeyCode.QUOTE);
                break;
            case ',':
                doType(KeyCode.COMMA);
                break;
            case '<':
                doType(KeyCode.SHIFT, KeyCode.COMMA);
                break;
            case '.':
                doType(KeyCode.PERIOD);
                break;
            case '>':
                doType(KeyCode.SHIFT, KeyCode.PERIOD);
                break;
            case '/':
                doType(KeyCode.SLASH);
                break;
            case '?':
                doType(KeyCode.SHIFT, KeyCode.SLASH);
                break;
            case ' ':
                doType(KeyCode.SPACE);
                break;
            default:
                throw new IllegalArgumentException("Cannot type character " + c);
        }
    }

    private void doType(KeyCode... keycodes) {
        if (keycodes.length > 1) {
            press(keycodes);
        }
        type(keycodes);
    }
}
