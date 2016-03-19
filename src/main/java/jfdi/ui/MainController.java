package jfdi.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import jfdi.storage.apis.TaskAttributes;
import jfdi.ui.Constants.MsgType;

public class MainController {

    public MainSetUp main;
    public IUserInterface ui;
    public CommandHandler cmdHandler;
    public Stage mainStage;
    public ObservableList<ListItem> importantList;

    @FXML
    public SplitPane leftRightSplit;
    @FXML
    public SplitPane upDownSplit;
    @FXML
    public TextField dayDisplayer;
    @FXML
    public TextArea statsDisplayer;
    @FXML
    public ListView<TaskAttributes> overdueList;
    @FXML
    public ListView<TaskAttributes> upcomingList;
    @FXML
    public ListView<ListItem> listMain;
    @FXML
    public TextArea fbArea;
    @FXML
    public TextArea cmdArea;

    public void initialize() {

        intSplitPanes();
        initDate();
        initImportantList();
        initStatsArea();
        initOverdueList();
        initUpcomingList();
        initFbArea();
        initCmdArea();
        initHelpList();

    }

    public void hideOverlays() {
        //noTaskOverlay.toBack();
        //helpOverlay.toBack();
        //noTaskOverlay.setOpacity(0);
        //helpOverlay.setOpacity(0);
    }

    public void clearCmdArea() {
        cmdArea.clear();
    }

    public void displayFb(String fb) {
        fbArea.appendText(fb);
    }

    public void relayFb(String fb, MsgType type) {
        clearFb();
        ui.displayFeedback(fb, type);
    }

    public void clearFb() {
        fbArea.clear();
    }

    public void displayWarning(String warning) {
        clearFb();
        fbArea.appendText(warning);
    }

    public void displayList() {
        ui.relayToLogic(Constants.CTRL_CMD_SHOWLIST);
    }

    public void setHighlights(HashSet<String> keywords) {
        // TODO Auto-generated method stub
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

    public int getIdFromIndex(int index) {
        return importantList.get(index).getItem().getId();
    }

    /***************************
     *** LEVEL 1 Abstraction ***
     ***************************/

    private void intSplitPanes() {
        leftRightSplit.lookupAll(".split-pane-divider").stream()
        .forEach(div ->  div.setMouseTransparent(true));

        upDownSplit.lookupAll(".split-pane-divider").stream()
        .forEach(div ->  div.setMouseTransparent(true));
    }

    public void initDate() {

        dayDisplayer.setMouseTransparent(true);
        dayDisplayer.setFocusTraversable(false);
        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        Calendar cal = Calendar.getInstance();
        dayDisplayer.setText(dateFormat.format(cal.getTime()));
    }

    private void initImportantList() {

        listMain.setMouseTransparent(true);
        listMain.setFocusTraversable(false);

        importantList = FXCollections.observableArrayList();
        listMain.setItems(importantList);

        /*listMain.setCellFactory(
                new Callback<ListView<ListItem>, ListCell<ListItem>>() {
                    @Override
                    public ListCell<ListItem> call(ListView<ListItem> param) {

                        ListCell<ListItem> cell = new ListCell<ListItem>() {
                            @Override
                            protected void updateItem(ListItem item, boolean bln) {
                                super.updateItem(item, bln);
                                if (item != null) {

                                }
                            }
                        };

                        return cell;
                    }
                });*/
    }

    private void initStatsArea() {
        statsDisplayer.setMouseTransparent(true);
        statsDisplayer.setFocusTraversable(false);
    }

    private void initOverdueList() {
        overdueList.setMouseTransparent(true);
        overdueList.setFocusTraversable(false);
    }

    private void initUpcomingList() {
        upcomingList.setMouseTransparent(true);
        upcomingList.setFocusTraversable(false);

    }

    private void initFbArea() {
        fbArea.setMouseTransparent(true);
        fbArea.setFocusTraversable(false);
        disableScrollBarFb();
    }

    private void initCmdArea() {

        cmdArea.setPromptText(Constants.CTRL_CMD_PROMPT_TEXT);
        handleEnterKey();
        disableScrollBarCmd();
    }

    private void initHelpList() {
        /*        helpList = FXCollections.observableArrayList();
        helpList.add(new HelpBox(HELP_ADD_DESC, HELP_ADD_COMMAND));
        helpList.add(new HelpBox(HELP_EDIT_DESC, HELP_EDIT_COMMAND));
        helpList.add(new HelpBox(HELP_DELETE_DESC, HELP_DELETE_COMMAND));
        helpList.add(new HelpBox(HELP_COMPLETE_DESC, HELP_COMPLETE_COMMAND));
        helpList.add(new HelpBox(HELP_INCOMPLETE_DESC, HELP_INCOMPLETE_COMMAND));
        helpList.add(new HelpBox(HELP_UNDO_DESC, HELP_UNDO_COMMAND));
        helpList.add(new HelpBox(HELP_SET_SAVE_LOCATION_DESC,
                                 HELP_SET_SAVE_LOCATION_COMMAND));
        helpList.add(new HelpBox(HELP_MOVE_SAVE_LOCATION_DESC,
                                 HELP_MOVE_SAVE_LOCATION_COMMAND));
        helpList.add(new HelpBox(HELP_SEARCH_DESC, HELP_SEARCH_COMMAND));
        helpList.add(new HelpBox(HELP_DISPLAY_INCOMPLETE_DESC,
                                 HELP_DISPLAY_INCOMPLETE_COMMAND));
        helpList.add(new HelpBox(HELP_DISPLAY_COMPLETE_DESC,
                                 HELP_DISPLAY_COMPLETE_COMMAND));
        helpList.add(new HelpBox(HELP_EXIT_DESC, HELP_EXIT_COMMAND));*/
    }

    private void initHelpOverlay() {
        /*        helpOverlay.toFront();
        helpOverlayIcon.setText(HELP_OVERLAY_ICON);
        helpOverlayTitle.setText(HELP_OVERLAY_TITLE);
        helpOverlayContents.setItems(helpList);*/
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
    private void disableScrollBarCmd() {
        cmdArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (cmdArea.lookup(".scroll-bar") != null) {
                ScrollBar scrollBarv = (ScrollBar) cmdArea.lookup(".scroll-bar");
                scrollBarv.setDisable(false);
                scrollBarv.setId("command-scroll-bar");
            }
        });
    }

    /**
     * Disable the scroll bar when it appears (Edit if necessary)
     */
    private void disableScrollBarFb() {
        fbArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (fbArea.lookup(".scroll-bar") != null) {
                ScrollBar scrollBarv = (ScrollBar) fbArea.lookup(".scroll-bar");
                scrollBarv.setDisable(false);
                scrollBarv.setId("command-scroll-bar");
            }
        });
    }

    /***************************
     *** LEVEL 3 Abstraction ***
     ***************************/
}
