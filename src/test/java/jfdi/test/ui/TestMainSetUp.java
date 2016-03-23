package jfdi.test.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import com.google.common.io.Files;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfdi.ui.Constants;
import jfdi.ui.IUserInterface;
import jfdi.ui.MainController;
import jfdi.ui.UI;
import jfdi.ui.items.ListItem;
import jfdi.ui.items.StatsItem;

public class TestMainSetUp extends ApplicationTest {

    /* The widgets of the GUI used for the tests. */
    Label dayDisplayer;
    ListView<StatsItem> statsDisplayer;
    Label listStatus;
    ListView<ListItem> listMain;
    TextArea fbArea;
    TextField cmdArea;

    VBox helpOverLay;
    Stage stage;
    Scene scene;
    Parent rootLayout;
    AnchorPane listLayout;
    MainController controller;

    /* This operation comes from ApplicationTest and loads the GUI to test. */
    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;
        this.stage.setTitle("JFDI");

        //loadFonts();
        initRootLayout();
        initView();
    }

    private void initRootLayout() throws IOException {

        rootLayout = FXMLLoader.load(getClass().getResource(Constants.URL_ROOT_PATH));
        Scene scene = new Scene(rootLayout);
        stage.setScene(scene);
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Hammersmith+One");
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Titillium+Web:200");
        stage.show();
        stage.setResizable(false);
        /* Put the GUI in front of windows. So that the robots will not interact with another
        window */
        stage.toFront();
    }

    private void initView() throws IOException, InterruptedException {

        IUserInterface ui = UI.getInstance();

        // Load View
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(Constants.URL_LIST_PATH));
        listLayout = (AnchorPane) loader.load();

        // Initialize Controller
        controller = loader.getController();
        controller.initialize();

        // Link UI with Controller
        ui.setController(controller);
        ui.init();

        ((BorderPane) rootLayout).setCenter(listLayout);
        controller.setStage(stage);

        // Link Controller with UI, MainSetUp and CommandHandler
        controller.setUi(ui);

        controller.importantList.removeAll(controller.importantList);

        File tempDir = Files.createTempDir();
        controller.displayList("use " + tempDir.getPath());

        controller.displayList(Constants.CTRL_CMD_INCOMPLETE);
        ui.displayWelcome();
    }

    /* Just a shortcut to retrieve widgets in the GUI. */
    public <T extends Node> T find(final String query) {
        /** TestFX provides many operations to retrieve elements from the loaded GUI. */
        return lookup(query).queryFirst();
    }

    @Before
    public void setUp() {
        /* Retrieve the tested widgets from the GUI. */
        dayDisplayer = find("#dayDisplayer");
        listStatus = find("#listStatus");
        listMain = find("#listMain");
        fbArea = find("#fbArea");
        cmdArea = find("#cmdArea");
        helpOverLay = find("#helpOverLay");

    }

    /* To clear the ongoing events */
    @After
    public void tearDown() throws TimeoutException {
        /* Close the window. It will be re-opened at the next test. */
        FxToolkit.hideStage();
        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }

    @Test
    public void testWidgetsExist() {
        final String errMsg = " %s cannot be retrieved!";

        assertNotNull(String.format(errMsg, "dayDisplayer"), dayDisplayer);
        assertNotNull(String.format(errMsg, "listStatus"), listStatus);
        assertNotNull(String.format(errMsg, "listMain"), listMain);
        assertNotNull(String.format(errMsg, "fbArea"), fbArea);
        assertNotNull(String.format(errMsg, "cmdArea"), cmdArea);
        //assertNotNull(String.format(errMsg, "helpOverLay"), helpOverLay);
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
                fbArea.getText(), "\nJ.F.D.I. : " + String.format(
                        Constants.CMD_SUCCESS_ADDED, controller.importantList.size(), "hello"));
    }
}
