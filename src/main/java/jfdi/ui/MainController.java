package jfdi.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

import com.sun.javafx.scene.control.skin.ListViewSkin;
import com.sun.javafx.scene.control.skin.VirtualFlow;

import edu.emory.mathcs.backport.java.util.Collections;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import jfdi.logic.ControlCenter;
import jfdi.storage.apis.TaskAttributes;
import jfdi.ui.Constants.CallType;
import jfdi.ui.Constants.ListStatus;
import jfdi.ui.Constants.MsgType;
import jfdi.ui.commandhandlers.CommandHandler;
import jfdi.ui.items.HelpItem;
import jfdi.ui.items.ListItem;

public class MainController {

    @FXML
    public Rectangle incompleteBox;
    @FXML
    public Rectangle overdueBox;
    @FXML
    public Rectangle upcomingBox;
    @FXML
    public Rectangle allBox;
    @FXML
    public Rectangle completedBox;
    @FXML
    public Rectangle searchBox;
    @FXML
    public Rectangle surpriseBox;
    @FXML
    public Rectangle helpBox;
    @FXML
    public Label incompleteTab;
    @FXML
    public Label overdueTab;
    @FXML
    public Label upcomingTab;
    @FXML
    public Label allTab;
    @FXML
    public Label completedTab;
    @FXML
    public Label searchTab;
    @FXML
    public Label surpriseTab;
    @FXML
    public Label helpTab;
    @FXML
    public Label incompleteCount;
    @FXML
    public Label overdueCount;
    @FXML
    public Label upcomingCount;
    @FXML
    public Label dayDisplayer;
    @FXML
    public ListView<ListItem> listMain;
    @FXML
    public TextArea fbArea;
    @FXML
    public AutoCompleteTextField cmdArea;
    @FXML
    public ListView<HelpItem> helpContent;
    @FXML
    public AnchorPane surpriseOverlay;
    @FXML
    public Label surpriseTitle;
    @FXML
    public Label taskDesc;
    @FXML
    public Label taskTime;
    @FXML
    public Label surpriseBottom;
    @FXML
    public AnchorPane noSurpriseOverlay;

    public MainSetUp main;
    public IUserInterface ui;
    public CommandHandler cmdHandler;
    public Stage mainStage;
    public ControlCenter controlCenter = ControlCenter.getInstance();
    public ObservableList<ListItem> importantList;
    public ListStatus displayStatus = ListStatus.INCOMPLETE;
    public ListStatus beforeHelp = ListStatus.INCOMPLETE;
    public String searchCmd;
    public boolean isUpdate = false;
    public StringProperty incompletePlaceHdr = new SimpleStringProperty();
    public StringProperty overduePlaceHdr = new SimpleStringProperty();
    public StringProperty upcomingPlaceHdr = new SimpleStringProperty();

    private ObservableList<HelpItem> helpList;
    private InputHistory inputHistory;
    private int firstVisibleId;
    private LinkedList<CallType> callQueue = new LinkedList<CallType>();

    public void initialize() {
        initDate();
        initImportantList();
        initFbArea();
        initCmdArea();
        initHelpList();
        initInputHistory();
    }

    public void hideOverlays() {
        helpContent.toBack();
        helpContent.setOpacity(0);
        surpriseOverlay.toBack();
        surpriseOverlay.setOpacity(0);
        noSurpriseOverlay.toBack();
        noSurpriseOverlay.setOpacity(0);
    }

    public void clearCmdArea() {
        cmdArea.clear();
    }

    public void displayFb(String fb) {
        fbArea.appendText(fb);
        String trimmedText = fbArea.getText().trim();
        fbArea.setText(trimmedText);
    }

    public void relayFb(String fb, MsgType type) {
        clearFb();
        ui.displayFeedback(fb, type);
    }

    public void appendFb(String fb, MsgType type) {
        ui.appendFeedback(fb, type);
    }

    public void clearFb() {
        fbArea.clear();
    }

    public void displayList(String cmd) {
        ui.processInput(cmd);
    }

    public void showHelpDisplay() {
        helpContent.toFront();
        helpContent.setOpacity(1);
    }

    public void showSurpriseDisplay() {
        surpriseOverlay.toFront();
        surpriseOverlay.setOpacity(1);
    }

    public void showNoSurpriseDisplay() {
        noSurpriseOverlay.toFront();
        noSurpriseOverlay.setOpacity(1);
        System.out.println("HERE");
    }

    public void switchTabSkin() {
        setAllTabsOff();
        switch (displayStatus) {
            case INCOMPLETE:
                incompleteBox.getStyleClass().setAll("tabOn");
                incompleteTab.getStyleClass().setAll("incompleteTabOn");
                break;
            case OVERDUE:
                overdueBox.getStyleClass().setAll("tabOn");
                overdueTab.getStyleClass().setAll("overdueTabOn");
                break;
            case UPCOMING:
                upcomingBox.getStyleClass().setAll("tabOn");
                upcomingTab.getStyleClass().setAll("upcomingTabOn");
                break;
            case ALL:
                allBox.getStyleClass().setAll("tabOn");
                allTab.getStyleClass().setAll("allTabOn");
                break;
            case COMPLETE:
                completedBox.getStyleClass().setAll("tabOn");
                completedTab.getStyleClass().setAll("completedTabOn");
                break;
            case SEARCH:
                searchBox.getStyleClass().setAll("tabOn");
                searchTab.getStyleClass().setAll("searchTabOn");
                break;
            case SURPRISE:
                surpriseBox.getStyleClass().setAll("tabOn");
                surpriseTab.getStyleClass().setAll("surpriseTabOn");
                break;
            case HELP:
                helpBox.getStyleClass().setAll("tabOn");
                helpTab.getStyleClass().setAll("helpTabOn");
                break;
            default:
                break;
        }
    }

    public void transListCmd() {
        switch (displayStatus) {
            case INCOMPLETE:
                listTasks(controlCenter.getIncompleteTasks(), false);
                break;
            case OVERDUE:
                listTasks(controlCenter.getOverdueTasks(), false);
                break;
            case UPCOMING:
                listTasks(controlCenter.getUpcomingTasks(), false);
                break;
            case ALL:
                listTasks(controlCenter.getAllTasks(), false);
                break;
            case COMPLETE:
                listTasks(controlCenter.getCompletedTasks(), false);
                break;
            case SEARCH:
                displayList(searchCmd);
                break;
            case SURPRISE:
                showSurpriseDisplay();
                break;
            case HELP:
                hideOverlays();
                break;
            default:
                break;
        }
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
        if (index < 0 || importantList.size() - 1 < index) {
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

        listMain.setFocusTraversable(false);
        importantList = FXCollections.observableArrayList();
        listMain.setItems(importantList);
        switchContext(ListStatus.INCOMPLETE, true);
    }

    private void initFbArea() {
        fbArea.setFocusTraversable(false);
        fbArea.setEditable(false);
        fbArea.setStyle("-fx-text-fill: #eeeeee;");
    }

    private void initCmdArea() {
        cmdArea.setPromptText(Constants.CTRL_CMD_PROMPT_TEXT);
        handleKeyPressedEvents();
        disableScrollBarCmd();
        updateAutoCompleteList();
    }

    private void initHelpList() {
        helpList = FXCollections.observableArrayList();
        helpList.add(new HelpItem(Constants.HELP_HOT_KEYS_DESC, Constants.HELP_HOT_KEYS_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_UP_DOWN_DESC, Constants.HELP_UP_DOWN_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_PAGE_UP_DOWN_DESC, Constants.HELP_PAGE_UP_DOWN_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_UP_DOWN_DESC, Constants.HELP_UP_DOWN_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_ADD_FLOATING_DESC, Constants.HELP_ADD_FLOATING_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_ADD_POINT_DESC, Constants.HELP_ADD_POINT_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_ADD_DEADLINE_DESC, Constants.HELP_ADD_DEADLINE_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_ADD_EVENT_DESC, Constants.HELP_ADD_EVENT_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_LIST_DESC, Constants.HELP_LIST_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_RENAME_DESC, Constants.HELP_RENAME_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_RESCH_DESC, Constants.HELP_RESCH_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_REMOVE_TIME_DESC, Constants.HELP_REMOVE_TIME_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_DONE_DESC, Constants.HELP_DONE_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_UNDONE_DESC, Constants.HELP_UNDONE_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_DELETE_DESC, Constants.HELP_DELETE_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_SEARCH_DESC, Constants.HELP_SEARCH_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_UNDO_DESC, Constants.HELP_UNDO_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_CREATE_ALIAS_DESC, Constants.HELP_CREATE_ALIAS_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_DELETE_ALIAS_DESC, Constants.HELP_DELETE_ALIAS_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_WILDCARD_DESC, Constants.HELP_WILDCARD_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_CHECK_DIR_DESC, Constants.HELP_CHECK_DIR_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_USE_DIR_DESC, Constants.HELP_USE_DIR_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_MOVE_DIR_DESC, Constants.HELP_MOVE_DIR_COMMAND));
        helpList.add(new HelpItem(Constants.HELP_EXIT_DESC, Constants.HELP_EXIT_COMMAND));
        helpContent.setItems(helpList);
    }

    private void initInputHistory() {
        inputHistory = new InputHistory();
    }

    public void initNotiBubbles() {
        updateNotiBubbles();
        Service<Void> service = new Service<Void>() {
            @Override
            public Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    public Void call() throws Exception {
                        // Background work
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    updateNotiBubbles();
                                } finally {
                                    latch.countDown();
                                }
                            }
                        });
                        latch.await();
                        // Keep with the background work
                        return null;
                    }
                };
            }
        };
        service.start();
    }

    public void initSurpriseOverlay(TaskAttributes task) {
        taskDesc.setText(task.getDescription());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy h:mma");
        if (task.getStartDateTime() == null) {
            if (task.getEndDateTime() == null) {
                // Floating Tasks
                taskTime.setText(Constants.ITEM_NO_TIMEDATE);
            } else {
                // Deadline Tasks
                String end = formatter.format(task.getEndDateTime());
                taskTime.setText(String.format(Constants.ITEM_DEADLINE, end));
            }
        } else {
            String start = formatter.format(task.getStartDateTime());
            if (task.getEndDateTime() == null) {
                // Point Tasks
                taskTime.setText(String.format(Constants.ITEM_POINT_TASK, start));
            } else {
                // Event Tasks
                String end = formatter.format(task.getEndDateTime());
                taskTime.setText(String.format(Constants.ITEM_EVENT_TASK, start, end));
            }
        }
    }

    /***************************
     *** LEVEL 2 Abstraction ***
     ***************************/

    public void updateAutoCompleteList() {
        cmdArea.setKeywords(ControlCenter.getInstance().getKeywords());
    }

    private void setAllTabsOff() {
        incompleteBox.getStyleClass().setAll("tabOff");
        incompleteTab.getStyleClass().setAll("incompleteTab");
        overdueBox.getStyleClass().setAll("tabOff");
        overdueTab.getStyleClass().setAll("overdueTab");
        upcomingBox.getStyleClass().setAll("tabOff");
        upcomingTab.getStyleClass().setAll("upcomingTab");
        allBox.getStyleClass().setAll("tabOff");
        allTab.getStyleClass().setAll("allTab");
        completedBox.getStyleClass().setAll("tabOff");
        completedTab.getStyleClass().setAll("completedTab");
        searchBox.getStyleClass().setAll("tabOff");
        searchTab.getStyleClass().setAll("searchTab");
        surpriseBox.getStyleClass().setAll("tabOff");
        surpriseTab.getStyleClass().setAll("surpriseTab");
        helpBox.getStyleClass().setAll("tabOff");
        helpTab.getStyleClass().setAll("helpTab");
    }

    @FXML
    private void handleKeyPressedEvents() {

        cmdArea.setOnKeyPressed((keyEvent) -> {
            KeyCode code = keyEvent.getCode();

            if (code == KeyCode.ENTER) {
                enterRoutine();
            } else if (code == KeyCode.UP) {
                getPreviousInput();
            } else if (code == KeyCode.DOWN) {
                getNextInput();
            } else if (code == KeyCode.PAGE_DOWN) {
                scrollDown();
            } else if (code == KeyCode.PAGE_UP) {
                scrollUp();
            } else if (code == KeyCode.TAB) {
                cmdArea.selectFirst();
            } else if (code == KeyCode.F1) {
                displayList(Constants.CTRL_CMD_INCOMPLETE);
            } else if (code == KeyCode.F2) {
                displayList(Constants.CTRL_CMD_OVERDUE);
            } else if (code == KeyCode.F3) {
                displayList(Constants.CTRL_CMD_UPCOMING);
            } else if (code == KeyCode.F4) {
                displayList(Constants.CTRL_CMD_ALL);
            } else if (code == KeyCode.F5) {
                displayList(Constants.CTRL_CMD_COMPLETE);
            } else if (code == KeyCode.F6) {
                displayList(Constants.CTRL_CMD_SURPRISE);
            } else if (code == KeyCode.F7) {
                displayList(Constants.CTRL_CMD_HELP);
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

    public void updateNotiBubbles() {
        incompletePlaceHdr.set(String.valueOf(controlCenter.getIncompleteTasks().size()));
        overduePlaceHdr.set(String.valueOf(controlCenter.getOverdueTasks().size()));
        upcomingPlaceHdr.set(String.valueOf(controlCenter.getUpcomingTasks().size()));
    }

    /***************************
     *** LEVEL 3 Abstraction ***
     ***************************/

    void enterRoutine() {
        cmdArea.hidePopup();
        String text = cmdArea.getText();
        UI.getInstance().processInput(text);
        inputHistory.addInput(text);
        cmdArea.clear();
    }

    private void getNextInput() {
        String nextInput = inputHistory.getNext();
        if (nextInput != null) {
            cmdArea.setText(nextInput);
            cmdArea.hidePopup();
            cmdArea.positionCaret(nextInput.length());
        }
    }

    private void getPreviousInput() {
        String previousInput = inputHistory.getPrevious();
        if (previousInput != null) {
            cmdArea.setText(previousInput);
            cmdArea.hidePopup();
            cmdArea.positionCaret(previousInput.length());
        }
    }

    private void scrollUp() {
        setFirstVisibleId();
        ListView<?> listView = getCurrentListView();
        listView.scrollTo(firstVisibleId - 1);
    }

    private void scrollDown() {
        setFirstVisibleId();
        ListView<?> listView = getCurrentListView();
        listView.scrollTo(firstVisibleId + 1);
    }

    private ListView<?> getCurrentListView() {
        if (displayStatus.equals(ListStatus.HELP)) {
            return helpContent;
        }
        return listMain;
    }

    public void setFirstVisibleId() {
        ListViewSkin<?> listViewSkin = (ListViewSkin<?>) getCurrentListView().getSkin();
        VirtualFlow<?> virtualFlow = (VirtualFlow<?>) listViewSkin.getChildren().get(0);
        IndexedCell<?> firstVisibleCell = virtualFlow.getFirstVisibleCellWithinViewPort();
        if (firstVisibleCell != null) {
            firstVisibleId = firstVisibleCell.getIndex();
        }
    }

    /**
     * Sets the display list to the given ArrayList of tasks which match the
     * context.
     *
     * @param tasks
     *            the ArrayList of tasks to be displayed
     */
    public void listTasks(ArrayList<TaskAttributes> tasks, boolean shouldCheckContext) {
        importantList.clear();
        for (TaskAttributes task : tasks) {
            appendTaskToDisplayList(task, shouldCheckContext);
        }
    }

    /**
     * Appends a task to the list of tasks displayed.
     *
     * @param task
     *            the task to be appended
     */
    public void appendTaskToDisplayList(TaskAttributes task, boolean shouldCheckContext) {
        if (shouldCheckContext && !isSameContext(task)) {
            return;
        }

        int onScreenId = importantList.size() + 1;
        ListItem listItem;

        if (task.isCompleted()) {
            listItem = new ListItem(onScreenId, task, true);
            importantList.add(listItem);
            importantList.get(importantList.size() - 1).strikeOut();
            listItem.strikeOut();
        } else {
            listItem = new ListItem(onScreenId, task, false);
            importantList.add(listItem);
            importantList.get(importantList.size() - 1).getStyleClass().add("itemBox");
        }
    }

    public void sortDisplayList() {
        ArrayList<TaskAttributes> taskList = new ArrayList<TaskAttributes>();
        importantList.forEach(listItem -> taskList.add(listItem.getItem()));
        Collections.sort(taskList);
        listTasks(taskList, false);
    }

    public boolean shouldSort() {
        return displayStatus.equals(ListStatus.OVERDUE) || displayStatus.equals(ListStatus.UPCOMING);
    }

    private boolean isSameContext(TaskAttributes task) {
        switch (displayStatus) {
            case ALL:
                return true;
            case SEARCH:
                return false;
            case SURPRISE:
                return false;
            case HELP:
                return false;
            case COMPLETE:
                return task.isCompleted();
            case INCOMPLETE:
                return !task.isCompleted();
            case OVERDUE:
                return task.isOverdue();
            case UPCOMING:
                return task.isUpcoming();
            default:
                assert false;
                return false;
        }
    }

    public void switchContext(ListStatus status, Boolean isListing) {
        if (status.equals(ListStatus.HELP)) {
            beforeHelp = displayStatus;
        }
        displayStatus = status;
        if (isListing) {
            transListCmd();
        }
        switchTabSkin();
    }
}
