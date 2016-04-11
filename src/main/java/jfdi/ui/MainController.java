// @@author A0129538W

package jfdi.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.sun.javafx.scene.control.skin.ListViewSkin;
import com.sun.javafx.scene.control.skin.VirtualFlow;

import edu.emory.mathcs.backport.java.util.Collections;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import jfdi.common.utilities.JFDIRobot;
import jfdi.logic.ControlCenter;
import jfdi.storage.apis.TaskAttributes;
import jfdi.ui.Constants.ListStatus;
import jfdi.ui.Constants.MsgType;
import jfdi.ui.items.AutoCompleteTextField;
import jfdi.ui.items.HelpItem;
import jfdi.ui.items.ListItem;

public class MainController {

    /*********************
     *** FXML ELEMENTS ***
     *********************/

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

    /************************
     *** Member Variables ***
     ************************/

    public MainSetUp main;
    public IUserInterface ui;
    public Stage mainStage;
    public ControlCenter controlCenter;

    // Stores the content of the on-Screen display list
    public ObservableList<ListItem> importantList;

    // Maps on-Screen display index against ArrayList index
    public HashMap<Integer, Integer> indexMatch = new HashMap<>();

    // Keeps track of the last TaskAttribute variable and
    // its on-screen index that needs to be highlighted
    public TaskAttributes highLight;
    public int highLightIndex;

    public ListStatus displayStatus = ListStatus.INCOMPLETE;
    public ListStatus beforeHelp = ListStatus.INCOMPLETE;

    // Keeps tracks of the latest searchCmd for screen
    // refreshing purposes
    public String searchCmd;

    // Keeps track of the count displayed in the notification bubbles
    public StringProperty incompletePlaceHdr = new SimpleStringProperty();
    public StringProperty overduePlaceHdr = new SimpleStringProperty();
    public StringProperty upcomingPlaceHdr = new SimpleStringProperty();

    // Stores the content of the HELP overlay
    private ObservableList<HelpItem> helpList;

    private InputHistory inputHistory;

    // For scroll-bar testing
    private int firstVisibleId;

    /**********************
     *** Public Methods ***
     **********************/

    /**
     * Initializes all on-screen display components.
     */
    public void initialize() {
        initDate();
        initImportantList();
        initFbArea();
        initCmdArea();
        initHelpList();
        initInputHistory();
    }

    /**
     * Places all overlays to the back of the main display.
     */
    public void hideOverlays() {
        helpContent.toBack();
        helpContent.setVisible(false);
        helpContent.setOpacity(0);
        surpriseOverlay.toBack();
        surpriseOverlay.setVisible(false);
        surpriseOverlay.setOpacity(0);
        noSurpriseOverlay.toBack();
        noSurpriseOverlay.setVisible(false);
        noSurpriseOverlay.setOpacity(0);
    }

    /**
     * Erases all content in the input bar.
     */
    public void clearCmdArea() {
        cmdArea.clear();
    }

    /**
     * Erases all content in the feedback areas.
     */
    public void clearFb() {
        fbArea.clear();
    }

    /**
     * Relays the content of the feedback message to UI for formatting according
     * to the message type.
     *
     * @param fb
     *            the content of the feedback message
     * @param type
     *            the type of the message to be displayed such as SUCCESS,
     *            ERROR, WARNING etc.
     */
    public void relayFb(String fb, MsgType type) {
        clearFb();
        ui.displayFeedback(fb, type);
    }

    /**
     * Relays the content of the feedback message to UI for formatting according
     * to the message type without clearing the feedback area
     *
     * @param fb
     *            the content of the feedback message
     * @param type
     *            the type of the message to be displayed such as SUCCESS,
     *            ERROR, WARNING etc.
     */
    public void appendFb(String fb, MsgType type) {
        ui.appendFeedback(fb, type);
    }

    /**
     * Trims the feedback message and displays it in the feedback area.
     *
     * @param fb
     *            the formatted feedback message
     */
    public void displayFb(String fb) {
        fbArea.appendText(fb);
        String trimmedText = fbArea.getText().trim();
        fbArea.setText(trimmedText);
    }

    /**
     * For internally inputing command to trigger changes to the main display
     * list.
     *
     * @param cmd
     *            the internal command to be executed
     */
    public void displayList(String cmd) {
        ui.processInput(cmd);
    }

    /**
     * Brings the help overlay in front of the main display.
     */
    public void showHelpDisplay() {
        helpContent.toFront();
        helpContent.setVisible(true);
        helpContent.setOpacity(1);
    }

    /**
     * Brings the surprise overlay in front of the main display.
     */
    public void showSurpriseDisplay() {
        surpriseOverlay.toFront();
        surpriseOverlay.setVisible(true);
        surpriseOverlay.setOpacity(1);
    }

    /**
     * Brings the noSurprise overlay in front of the main display.
     */
    public void showNoSurpriseDisplay() {
        noSurpriseOverlay.toFront();
        noSurpriseOverlay.setVisible(true);
        noSurpriseOverlay.setOpacity(1);
    }

    /**
     * Getter for the first index visible on the on-screen main display list.
     *
     * @return the topmost visible on-screen index
     */
    public int getFirstVisibleId() {
        setFirstVisibleId();
        return firstVisibleId;
    }

    /**
     * Clears up the input history stack maintained by the UI.
     */
    public void clearInputHistory() {
        inputHistory.clearHistory();
    }

    /**
     * Switches the highlighting tab according to the current status of the
     * on-screen list display.
     */
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

    /**
     * Translates the current status of the on-screen list display to its
     * respective listing commands and executes it for the purpose of refreshing
     * the on-screen display.
     */
    public void transListCmd() {
        switch (displayStatus) {
            case INCOMPLETE:
                listTasks(controlCenter.getIncompleteTasks(), true);
                break;
            case OVERDUE:
                listTasks(controlCenter.getOverdueTasks(), true);
                sortDisplayList();
                break;
            case UPCOMING:
                listTasks(controlCenter.getUpcomingTasks(), true);
                sortDisplayList();
                break;
            case ALL:
                listTasks(controlCenter.getAllTasks(), true);
                break;
            case COMPLETE:
                listTasks(controlCenter.getCompletedTasks(), true);
                break;
            case SEARCH:
                displayList(searchCmd);
                break;
            case SURPRISE:
                showSurpriseDisplay();
                break;
            case SURPRISE_YAY:
                hideOverlays();
                listTasks(controlCenter.getIncompleteTasks(), true);
                doSurpriseYay();
                break;
            case HELP:
                hideOverlays();
                break;
            default:
                break;
        }
    }

    /**
     * Sets the display list to the given ArrayList of tasks which match the
     * context.
     *
     * @param tasks
     *            the ArrayList of tasks to be displayed
     */
    public void listTasks(ArrayList<TaskAttributes> tasks, boolean shouldClear) {

        if (shouldClear) {
            importantList.clear();
            indexMatch.clear();
        }

        if (tasks.isEmpty()) {
            return;
        }

        if (displayStatus.equals(ListStatus.INCOMPLETE)) {
            splitIncompleteList(tasks, false);
        } else {
            for (TaskAttributes task : tasks) {
                appendTaskToDisplayList(task, false);
            }
        }
    }

    /**
     * Appends a task to the list of tasks displayed.
     *
     * @param task
     *            the task to be appended
     * @param shouldCheckContext
     *            flag for context checking
     * @param shouldHighLight
     *            flag for item highlighting
     */
    public void appendTaskToDisplayList(TaskAttributes task, boolean shouldHighLight) {

        ListItem listItem;
        int index = indexMatch.size() + 1;

        if (task.isCompleted()) {
            listItem = new ListItem(index, task, true);
            importantList.add(listItem);
            importantList.get(importantList.size() - 1).strikeOut();
            listItem.strikeOut();
        } else {
            listItem = new ListItem(index, task, false);
            importantList.add(listItem);
            if (shouldHighLight) {
                importantList.get(importantList.size() - 1).getStyleClass().add("itemBoxYay");
                highLightIndex = importantList.get(importantList.size() - 1).getIndex();

            } else {
                importantList.get(importantList.size() - 1).getStyleClass().add("itemBox");
            }
        }
        indexMatch.put(index, importantList.size() - 1);
    }

    /**
     * Switches the status of the list view to intended one.
     *
     * @param status
     *            the intended list status
     * @param isListing
     *            flag for item listing
     */
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

    /**
     * Translates an on-screen display index of a task to its respective task
     * storage ID
     *
     * @param index
     *            the on-screen display index of a task
     * @return the storage ID of the task with the given on-screen display index
     */
    public int getIdFromIndex(int index) {
        if (index < 0 || indexMatch.size() - 1 < index) {
            return -1;
        }

        int actualIndex = indexMatch.get(index + 1);

        return importantList.get(actualIndex).getItem().getId();
    }

    /**
     * Updates the notification bubbles on display.
     */
    public void updateNotiBubbles() {
        incompletePlaceHdr.set(String.valueOf(controlCenter.getIncompleteTasks().size()));
        overduePlaceHdr.set(String.valueOf(controlCenter.getOverdueTasks().size()));
        upcomingPlaceHdr.set(String.valueOf(controlCenter.getUpcomingTasks().size()));
    }

    /**
     * Updates the set of keywords stored in the AutoComplete list.
     */
    public void updateAutoCompleteList() {
        cmdArea.setKeywords(ControlCenter.getInstance().getKeywords());
    }

    /**
     * Checks if the list to be displayed should be sorted.
     *
     * @return flag for list sorting
     */
    public boolean shouldSort() {
        return displayStatus.equals(ListStatus.OVERDUE) || displayStatus.equals(ListStatus.UPCOMING);
    }

    /**
     * Sorts the list of tasks on display.
     */
    public void sortDisplayList() {
        ArrayList<TaskAttributes> taskList = new ArrayList<TaskAttributes>();
        importantList.forEach(listItem -> taskList.add(listItem.getItem()));
        Collections.sort(taskList);
        listTasks(taskList, true);
    }

    /**
     * Executes the actions after an "ENTER" keyboard input.
     */
    public void enterRoutine() {
        cmdArea.hidePopup();
        String text = cmdArea.getText();
        UI.getInstance().processInput(text);
        inputHistory.addInput(text);
        cmdArea.clear();
    }

    /**
     * Links this controller instance to the only instance of MainSetUp.
     *
     * @param main
     *            the MainSetUp instance of the project
     */
    public void setMainApp(MainSetUp main) {
        this.main = main;
    }

    /**
     * Links this controller instance to the only instance of IUserInterface.
     *
     * @param ui
     *            the IUserInterface instance of the project
     */
    public void setUi(IUserInterface ui) {
        this.ui = ui;
    }

    /**
     * Allows this controller to have access to the primary stage of this
     * project.
     *
     * @param stage
     *            the primary stage of this project
     */
    public void setStage(Stage stage) {
        this.mainStage = stage;
    }

    /**
     * Allows this controller to have access to the ControlCenter of the Logic
     * component in this project.
     *
     * @param controlCenter
     *            the ControlCenter of the Logic component
     */
    public void setLogicControlCentre(ControlCenter controlCenter) {
        this.controlCenter = controlCenter;
    }

    /***************************
     *** LEVEL 1 Abstraction ***
     ***************************/

    /**
     * Initiates the date-displaying Label element.
     */
    private void initDate() {

        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        Calendar cal = Calendar.getInstance();
        dayDisplayer.setText(dateFormat.format(cal.getTime()));
    }

    /**
     * Initiates the ListView element of main display list by bonding it to an
     * observableList object.
     */
    private void initImportantList() {

        listMain.setFocusTraversable(false);
        importantList = FXCollections.observableArrayList();
        listMain.setItems(importantList);
    }

    /**
     * Initiates the feedback area TextArea element.
     */
    private void initFbArea() {
        fbArea.setFocusTraversable(false);
        fbArea.setEditable(false);
        fbArea.setStyle("-fx-text-fill: #eeeeee;");
    }

    /**
     * Initiates the command area AutoCompleteTextField element.
     */
    private void initCmdArea() {
        cmdArea.setPromptText(Constants.CTRL_CMD_PROMPT_TEXT);
        handleKeyPressedEvents();
        disableScrollBarCmd();
        updateAutoCompleteList();
    }

    /**
     * Initiates the help content that will be shown upon an input "help".
     */
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

    /**
     * Initiates the input history storage of the UI.
     */
    private void initInputHistory() {
        inputHistory = new InputHistory();
    }

    /**
     * Executes actions for a "yay" input after "surprise" event.
     */
    private void doSurpriseYay() {
        importantList.clear();
        indexMatch.clear();
        splitIncompleteList(controlCenter.getIncompleteTasks(), true);
        displayStatus = ListStatus.INCOMPLETE;
        switchTabSkin();
        listMain.scrollTo(highLightIndex);
        relayFb(Constants.CMD_SUCCESS_SURPRISED_YAY, MsgType.SUCCESS);
    }

    /**
     * Splits the incomplete list into OVERDUE, UPCOMING and OTHERS.
     *
     * @param items
     *            the list of tasks to be on display
     * @param shouldHighLight
     *            flag for item highlighting
     */
    private void splitIncompleteList(ArrayList<TaskAttributes> items, boolean shouldHighLight) {

        ArrayList<TaskAttributes> overdueList = new ArrayList<>(controlCenter.getOverdueTasks());
        ArrayList<TaskAttributes> upcomingList = new ArrayList<>(controlCenter.getUpcomingTasks());
        ArrayList<TaskAttributes> othersList = controlCenter.getIncompleteTasks().stream()
                .filter(task -> !task.isOverdue() && !task.isUpcoming())
                .collect(Collectors.toCollection(ArrayList::new));

        if (!overdueList.isEmpty()) {
            Collections.sort(overdueList);
            importantList.add(Constants.HEADER_OVERDUE);
            for (TaskAttributes task : overdueList) {
                if (shouldHighLight && task.equals(highLight)) {
                    appendTaskToDisplayList(task, shouldHighLight);
                    continue;
                }
                appendTaskToDisplayList(task, false);
            }
        }

        if (!upcomingList.isEmpty()) {
            Collections.sort(upcomingList);
            importantList.add(Constants.HEADER_UPCOMING);
            for (TaskAttributes task : upcomingList) {
                if (shouldHighLight && task.equals(highLight)) {
                    appendTaskToDisplayList(task, shouldHighLight);
                    continue;
                }
                appendTaskToDisplayList(task, false);
            }
        }

        if (!othersList.isEmpty()) {
            importantList.add(Constants.HEADER_OTHERS);
            for (TaskAttributes task : othersList) {
                if (shouldHighLight && task.getDescription().equals(highLight.getDescription())) {
                    appendTaskToDisplayList(task, shouldHighLight);
                    continue;
                }
                appendTaskToDisplayList(task, false);
            }
        }
    }

    /**
     * Shifts the list view on display to start with the indicated index.
     */
    private void setFirstVisibleId() {
        ListViewSkin<?> listViewSkin = (ListViewSkin<?>) getCurrentListView().getSkin();
        VirtualFlow<?> virtualFlow = (VirtualFlow<?>) listViewSkin.getChildren().get(0);
        IndexedCell<?> firstVisibleCell = virtualFlow.getFirstVisibleCellWithinViewPort();
        if (firstVisibleCell != null) {
            firstVisibleId = firstVisibleCell.getIndex();
        }
    }

    /***************************
     *** LEVEL 2 Abstraction ***
     ***************************/

    /**
     * Turns all tabs off (Dark color with light font).
     */
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

    /**
     * Handles the events triggered by relevant keyboard inputs.
     */
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
            } else if (code == KeyCode.F8) {
                JFDIRobot.typeNext();
            } else {
                return;
            }

            keyEvent.consume();
        });
    }

    /**
     * Disable the scroll bar in the command area when it appears.
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

    /***************************
     *** LEVEL 3 Abstraction ***
     ***************************/

    /**
     * Sets the next input in the input history in the command area.
     */
    private void getNextInput() {
        String nextInput = inputHistory.getNext();
        if (nextInput != null) {
            cmdArea.setText(nextInput);
            cmdArea.hidePopup();
            cmdArea.positionCaret(nextInput.length());
        }
    }

    /**
     * Sets the previous input in the input history in the command area.
     */
    private void getPreviousInput() {
        String previousInput = inputHistory.getPrevious();
        if (previousInput != null) {
            cmdArea.setText(previousInput);
            cmdArea.hidePopup();
            cmdArea.positionCaret(previousInput.length());
        }
    }

    /**
     * Performs the scroll-up action.
     */
    private void scrollUp() {
        setFirstVisibleId();
        ListView<?> listView = getCurrentListView();
        listView.scrollTo(firstVisibleId - 1);
    }

    /**
     * Performs the scroll-down action.
     */
    private void scrollDown() {
        setFirstVisibleId();
        ListView<?> listView = getCurrentListView();
        listView.scrollTo(firstVisibleId + 1);
    }

    /**
     * @return the current list content on-display under the overlays
     */
    private ListView<?> getCurrentListView() {
        if (displayStatus.equals(ListStatus.HELP)) {
            return helpContent;
        }
        return listMain;
    }
}
