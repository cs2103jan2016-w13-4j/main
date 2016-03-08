package jfdi.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfdi.storage.data.TaskAttributes;
import jfdi.ui.CommandHandler.MsgType;

public class MainController {

    private static final String CTRL_CMD_SHOWLIST = "list";

    public MainSetUp main;
    public IUserInterface ui;
    public CommandHandler cmdHandler;
    public Stage mainStage;
    public ObservableList<TaskAttributes> importantList;

    @FXML
    public TextField dayDisplayer;
    @FXML
    public TextArea fbArea;
    @FXML
    public TextArea cmdArea;
    @FXML
    public TableView<TaskAttributes> tableMain;
    @FXML
    public TableColumn<TaskAttributes, Integer> idCol;
    @FXML
    public TableColumn<TaskAttributes, String> taskCol;
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

    public void relayFb(String fb, MsgType type) {
        ui.displayFeedback(fb, type);
    }

    public void clearFb() {
        fbArea.clear();
    }

    public void displayWarning(String warning) {
        fbArea.appendText("\n");
        fbArea.appendText(warning);
    }

    public void displayList() {
        ui.relayToLogic(CTRL_CMD_SHOWLIST);
    }

    public void setMainApp(MainSetUp main) {
        this.main = main;
    }

    public void setUi(IUserInterface ui) {
        this.ui = ui;
    }

    public void setCmdHandler(CommandHandler cmdHandler) {
        this.cmdHandler = cmdHandler;
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

        importantList = FXCollections.observableArrayList();
        tableMain.setItems(importantList);

        idCol.setCellValueFactory(new Callback<CellDataFeatures<TaskAttributes, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(CellDataFeatures<TaskAttributes, Integer> param) {
                return  new ReadOnlyObjectWrapper<Integer>(param.getValue().getId());
            }
        });

        taskCol.setCellValueFactory(new Callback<CellDataFeatures<TaskAttributes, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<TaskAttributes, String> param) {
                return  new ReadOnlyObjectWrapper<String>(param.getValue().getDescription());
            }
        });
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
