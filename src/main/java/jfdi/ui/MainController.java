package jfdi.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;

import com.sun.javafx.scene.control.skin.ListViewSkin;
import com.sun.javafx.scene.control.skin.VirtualFlow;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import jfdi.storage.apis.TaskAttributes;
import jfdi.ui.Constants.MsgType;
import jfdi.ui.commandhandlers.CommandHandler;
import jfdi.ui.items.HelpItem;
import jfdi.ui.items.ListItem;
import jfdi.ui.items.StatsItem;

public class MainController {

    @FXML
    public VBox backDrop;
    @FXML
    public SplitPane leftRightSplit;
    @FXML
    public SplitPane upDownSplit;
    @FXML
    public Label dayDisplayer;
    @FXML
    public ListView<StatsItem> statsDisplayer;
    @FXML
    public Label overdueLabel;
    @FXML
    public Label upcomingLabel;
    @FXML
    public ListView<TaskAttributes> overdueList;
    @FXML
    public ListView<TaskAttributes> upcomingList;
    @FXML
    public Label listStatus;
    @FXML
    public ListView<ListItem> listMain;
    @FXML
    public TextArea fbArea;
    @FXML
    public TextField cmdArea;

    @FXML
    public VBox helpOverlay;
    @FXML
    public Label helpIcon;
    @FXML
    public Label helpTitle;
    @FXML
    public ListView<HelpItem> helpContent;

    public MainSetUp main;
    public IUserInterface ui;
    public CommandHandler cmdHandler;
    public Stage mainStage;
    public ObservableList<ListItem> importantList;
    public ObservableList<StatsItem> statsList;
    public int completedNum;
    public int dueTodayNum;

    private ObservableList<HelpItem> helpList;
    private Timeline overlayTimeline;
    private InputHistory inputHistory;
    private int firstVisibleId;
    private int lastVisibleId;

    public void initialize() {

        initDate();
        initImportantList();
        initStatsArea();
        initOverdueList();
        initUpcomingList();
        initFbArea();
        initCmdArea();
        initTimelines();
        initHelpList();
        initInputHistory();

    }

    public void hideOverlays() {
        //noTaskOverlay.toBack();
        helpOverlay.toBack();
        //noTaskOverlay.setOpacity(0);
        helpOverlay.setOpacity(0);
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

    public void showHelpDisplay() {
        hideOverlays();
        FadeTransition fadeIn = initFadeIn(helpOverlay,
                Constants.OVERLAY_FADE_IN_MILLISECONDS);

        overlayTimeline = generateHelpOverlayTimeline(fadeIn);
        overlayTimeline.play();
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
        if (index < 0 || importantList.size()-1 < index) {
            return -1;
        }
        return importantList.get(index).getItem().getId();
    }

    /***************************
     *** LEVEL 1 Abstraction ***
     ***************************/

    public void initDate() {

        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        Calendar cal = Calendar.getInstance();
        dayDisplayer.setText(dateFormat.format(cal.getTime()));
    }

    private void initImportantList() {

        listMain.setMouseTransparent(true);
        listMain.setFocusTraversable(false);
        importantList = FXCollections.observableArrayList();
        listMain.setItems(importantList);
        handleOverlays(importantList);

    }

    private void initStatsArea() {
        statsDisplayer.setMouseTransparent(true);
        statsDisplayer.setFocusTraversable(false);
        statsList = FXCollections.observableArrayList();
        statsDisplayer.setItems(statsList);
        statsList.add(new StatsItem(Constants.CTRL_STATS_NAME1));
        statsList.add(new StatsItem(Constants.CTRL_STATS_NAME2));
        completedNum = 0;
        dueTodayNum = 0;
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
        handleKeyPressedEvents();
        disableScrollBarCmd();
    }

    private void initTimelines() {
        //feedbackTimeline = new Timeline();
        overlayTimeline = new Timeline();
    }

    private void initHelpList() {
        helpList = FXCollections.observableArrayList();
        helpList.add(new HelpItem(Constants.HELP_ADD_FLOATING_DESC, Constants.HELP_ADD_FLOATING_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_ADD_POINT_DESC, Constants.HELP_ADD_POINT_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_ADD_DEADLINE_DESC, Constants.HELP_ADD_DEADLINE_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_ADD_EVENT_DESC, Constants.HELP_ADD_EVENT_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_LIST_INCOMPLETE_DESC, Constants.HELP_LIST_INCOMPLETE_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_LIST_COMPLETE_DESC, Constants.HELP_LIST_COMPLETE_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_LIST_ALL_DESC, Constants.HELP_LIST_ALL_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_RENAME_DESC, Constants.HELP_RENAME_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_RESCH_DESC, Constants.HELP_RESCH_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_REMOVE_TIME_DESC, Constants.HELP_REMOVE_TIME_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_DONE_DESC, Constants.HELP_DONE_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_UNDONE_DESC, Constants.HELP_UNDONE_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_DELETE_DESC, Constants.HELP_DELETE_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_SEARCH_DESC, Constants.HELP_SEARCH_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_REMINDER_DESC, Constants.HELP_REMINDER_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_UNDO_DESC, Constants.HELP_UNDO_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_CREATE_ALIAS_DESC, Constants.HELP_CREATE_ALIAS_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_DELETE_ALIAS_DESC, Constants.HELP_DELETE_ALIAS_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_WILDCARD_DESC, Constants.HELP_WILDCARD_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_CHECK_DIR_DESC, Constants.HELP_CHECK_DIR_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_USE_DIR_DESC, Constants.HELP_USE_DIR_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_MOVE_DIR_DESC, Constants.HELP_MOVE_DIR_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_UP_DOWN_DESC, Constants.HELP_UP_DOWN_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_EXIT_DESC, Constants.HELP_EXIT_COMMAND));
    }

    private void initHelpOverlay() {
        helpOverlay.toFront();
        helpIcon.setText(Constants.HELP_OVERLAY_ICON);
        helpTitle.setText(Constants.HELP_OVERLAY_TITLE);
        helpContent.setItems(helpList);
    }

    private Timeline generateHelpOverlayTimeline(FadeTransition fadeIn) {
        return new Timeline(new KeyFrame(new Duration(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                initHelpOverlay();
                fadeIn.play();
            }
        }, new KeyValue(helpOverlay.alignmentProperty(), Pos.CENTER_LEFT)));
    }

    private FadeTransition initFadeIn(Node node, int duration) {
        FadeTransition fadeIn = new FadeTransition(new Duration(duration));
        fadeIn.setNode(node);
        fadeIn.setToValue(1);
        return fadeIn;
    }

    private FadeTransition initFadeOut(Node node, int duration) {
        FadeTransition fadeOut = new FadeTransition(new Duration(duration));
        fadeOut.setNode(node);
        fadeOut.setToValue(0);
        return fadeOut;
    }

    private void initInputHistory() {
        inputHistory = new InputHistory();
    }

    /***************************
     *** LEVEL 2 Abstraction ***
     ***************************/

    private void handleOverlays(ObservableList<ListItem> tasks) {
        hideOverlays();
        //if (tasks.isEmpty()) {
        //showNoTaskOverlay();
        //}
    }

    @FXML
    private void handleKeyPressedEvents() {
        cmdArea.setOnKeyPressed((keyEvent) -> {
            KeyCode code = keyEvent.getCode();

            if (code == KeyCode.ENTER) {
                String text = cmdArea.getText();
                ui.processInput(text);
                inputHistory.addInput(text);
                cmdArea.clear();
            } else if (code == KeyCode.UP) {
                String previousInput = inputHistory.getPrevious();
                if (previousInput != null) {
                    cmdArea.setText(previousInput);
                    cmdArea.positionCaret(previousInput.length());
                }
            } else if (code == KeyCode.DOWN) {
                String nextInput = inputHistory.getNext();
                if (nextInput != null) {
                    cmdArea.setText(nextInput);
                    cmdArea.positionCaret(nextInput.length());
                }
            } else if (code == KeyCode.PAGE_DOWN) {
                setFirstAndLastVisibleIds();
                listMain.scrollTo(lastVisibleId);
            } else if (code == KeyCode.PAGE_UP) {
                setFirstAndLastVisibleIds();
                int scrollAmount = lastVisibleId - firstVisibleId;
                listMain.scrollTo(firstVisibleId - scrollAmount);
            } else {
                return;
            }

            keyEvent.consume();

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

    public void setFirstAndLastVisibleIds() {
        ListViewSkin<?> listViewSkin = (ListViewSkin<?>) listMain.getSkin();
        VirtualFlow<?> virtualFlow = (VirtualFlow<?>) listViewSkin.getChildren().get(0);
        firstVisibleId = virtualFlow.getFirstVisibleCell().getIndex();
        lastVisibleId = virtualFlow.getLastVisibleCell().getIndex();
    }
}
