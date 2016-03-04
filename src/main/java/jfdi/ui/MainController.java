package jfdi.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class MainController {

    public MainSetUp main;
    public IUserInterface ui;
    public Stage mainStage;

    @FXML
    public TextField dayDisplayer;
    @FXML
    public TextArea fbArea;
    @FXML
    public TextArea cmdArea;
    @FXML
    private ListView<String> listBoxMain;
    @FXML
    private TextField txtAddItem;

    public void initialize() {

        initDate();
        initList();
        initFbArea();
        initCmdArea();

    }

    public void clearCmdArea() {
        cmdArea.clear();
    }

    public void displayFb(String fb) {
        fbArea.appendText("\n");
        fbArea.appendText(fb);
    }

    public void clearFb() {
        fbArea.clear();
    }

    public void displayWarning(String warning) {
        fbArea.appendText("\n");
        fbArea.appendText(warning);
    }

    public void setMainApp(MainSetUp main) {
        this.main = main;
    }

    public void setUi(IUserInterface ui) {
        this.ui = ui;
    }

    public void setStage(Stage stage) {
        this.mainStage = stage;
    }

    /***************************
     *** LEVEL 1 Abstraction ***
     ***************************/

    public void initDate() {

        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        Calendar cal = Calendar.getInstance();
        dayDisplayer.setText(dateFormat.format(cal.getTime()));
    }

    private void initList() {

    }

    private void initFbArea() {

        fbArea.setText("J.F.D.I. : Hello Jim! Nice to see you again! :)");
    }

    private void initCmdArea() {

        cmdArea.setPromptText("(Hey Jim! Please let me know what I can do for you!)");
        handleEnterKey();
        disableScrollBar();
    }

    /***************************
     *** LEVEL 2 Abstraction ***
     ***************************/
    @FXML
    private void handleEnterKey() {
        cmdArea.setOnKeyPressed((keyEvent) -> {
            KeyCode code = keyEvent.getCode();

            if (code == KeyCode.ENTER) {
                String text = cmdArea.getText();
                ui.processInput(text);
                cmdArea.clear();
                // consume the new line left in the command area
                keyEvent.consume();
            }

            /*
             * Not needed yet! // Tab event is sent to UI whenever tab is hit if
             * (code == KeyCode.TAB) { ui.passKeyEvent(code); // consume the tab
             * space left in the command area keyEvent.consume(); }
             */
        });

    }

    /**
     * Disable the scroll bar when it appears (Edit if necessary)
     */
    private void disableScrollBar() {
        cmdArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (cmdArea.lookup(".scroll-bar") != null) {
                ScrollBar scrollBarv = (ScrollBar) cmdArea.lookup(".scroll-bar");
                scrollBarv.setDisable(false);
                scrollBarv.setId("command-scroll-bar");
            }
        });
    }

    /***************************
     *** LEVEL 3 Abstraction ***
     ***************************/
}
