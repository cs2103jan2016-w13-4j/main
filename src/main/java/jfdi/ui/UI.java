// @@author A0129538W

package jfdi.ui;

import com.google.common.eventbus.EventBus;

import jfdi.logic.ControlCenter;
import jfdi.ui.Constants.ListStatus;
import jfdi.ui.Constants.MsgType;
import jfdi.ui.commandhandlers.CommandHandler;

public class UI implements IUserInterface {

    private static final EventBus eventBus = new EventBus();
    private static UI ourInstance = new UI();
    private static ControlCenter logic;

    public MainController controller;

    private UI() {
    }

    public static UI getInstance() {
        return ourInstance;
    }

    @Override
    public void init() {

        // Initialize Logic
        logic = ControlCenter.getInstance();
        controller.setLogicControlCentre(logic);
        prepareListeners();
    }

    @Override
    public void displayWelcome() {
        controller.clearFb();
        showToUser(Constants.UI_MESSAGE_GREETING);
        showToUser(Constants.UI_MESSAGE_WELCOME);
    }

    @Override
    public void processInput(String input) {
        controller.hideOverlays();
        controller.cmdArea.clear();

        if (controller.displayStatus.equals(ListStatus.HELP)) {
            controller.hideOverlays();
            controller.displayStatus = controller.beforeHelp;
            if (controller.beforeHelp == ListStatus.SURPRISE) {
                controller.displayStatus = ListStatus.INCOMPLETE;
            }
            controller.switchTabSkin();
        }

        if (controller.displayStatus.equals(ListStatus.SURPRISE)) {
            if (input.equalsIgnoreCase(Constants.CTRL_CMD_SURPRISE_NAY)) {
                input = Constants.CTRL_CMD_SURPRISE;
            } else if (input.equalsIgnoreCase(Constants.CTRL_CMD_SURPRISE_YAY)) {
                controller.switchContext(ListStatus.SURPRISE_YAY, true);
                return;
            }
        }

        // Relay user input to logic and wait for reply
        relayToLogic(input);
    }

    @Override
    public void relayToLogic(String input) {
        // Relay user input to logic and wait for reply
        logic.handleInput(input);
    }

    @Override
    public void displayFeedback(String fb, MsgType type) {
        appendFeedback(fb, type);
    }

    @Override
    public void appendFeedback(String fb, MsgType type) {
        switch (type) {
            case SUCCESS:
                showToUser(String.format(Constants.UI_MESSAGE_RESPONSE, fb));
                break;
            case WARNING:
                showToUser(String.format(Constants.UI_MESSAGE_WARNING, fb));
                break;
            case ERROR:
                showToUser(String.format(Constants.UI_MESSAGE_ERROR, fb));
                break;
            case EXIT:
                showToUser(Constants.UI_MESSAGE_QUIT);
                break;
            default:
                break;
        }

    }

    @Override
    public int getTaskId(int onScreenId) {
        return controller.getIdFromIndex(onScreenId - 1);
    }

    /**
     * Calls method to execute actions for an "ENTER" keyboard input.
     */
    public void triggerEnter() {
        controller.enterRoutine();
    }

    /**
     * Getter for the eventBus object (for Logic use)
     *
     * @return event bus object.
     */
    public static EventBus getEventBus() {
        return eventBus;
    }

    /***************************
     *** LEVEL 1 Abstraction ***
     ***************************/

    /**
     * Calls method to register all command handlers.
     */
    private void prepareListeners() {
        CommandHandler.registerEvents();
    }

    /**
     * Calls method to display feedback on the screen.
     *
     * @param string
     *            feedback to be displayed
     */
    private void showToUser(String string) {
        controller.displayFb(string);
        System.out.println(string);
    }
}
