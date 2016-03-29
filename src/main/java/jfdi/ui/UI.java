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

        // showToUser(UI_MESSAGE_INIT);

        // Initialize Logic
        logic = ControlCenter.getInstance();
        prepareListeners();

        // showToUser(UI_MESSAGE_INITED);
    }

    @Override
    public void displayWelcome() {
        controller.clearFb();
        showToUser(Constants.UI_MESSAGE_GREETING);
        showToUser(Constants.UI_MESSAGE_WELCOME);
    }

    @Override
    public void processInput(String input) {

        if (controller.displayStatus.equals(ListStatus.HELP)) {
            controller.hideOverlays();
            controller.displayStatus = controller.beforeHelp;
            controller.switchTabSkin();
        }

        if (controller.displayStatus.equals(ListStatus.SURPRISE)) {
            if (input.equalsIgnoreCase(Constants.CTRL_CMD_SURPRISE_NAY)) {
                input = Constants.CTRL_CMD_SURPRISE;
            } else if (input.equalsIgnoreCase(Constants.CTRL_CMD_SURPRISE_YAY)) {
                displayFeedback(Constants.CMD_SUCCESS_SURPRISED_YAY, MsgType.SUCCESS);
                return;
            }
            controller.hideOverlays();
            controller.switchTabSkin();
        }

        // Clear controller first
        controller.clearCmdArea();
        controller.clearFb();

        // Show user what the command recognized in the feedback area
        // controller.displayFb(String.format(Constants.UI_MESSAGE_USERCMD,
        // input));

        // Relay user input to logic and wait for reply
        relayToLogic(input);
    }

    @Override
    public void displayFeedback(String fb, MsgType type) {
        controller.clearFb();
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
                showToUser(fb);
                break;
        }

    }

    @Override
    public int getTaskId(int onScreenId) {
        return controller.getIdFromIndex(onScreenId - 1);
    }

    /***************************
     *** LEVEL 1 Abstraction ***
     ***************************/

    private void prepareListeners() {
        CommandHandler.registerEvents();
    }

    private void showToUser(String string) {
        controller.displayFb(string);
        System.out.println(string);
    }

    @Override
    public void relayToLogic(String input) {
        // Relay user input to logic and wait for reply
        logic.handleInput(input);
    }

    public static EventBus getEventBus() {
        return eventBus;
    }

    public void triggerEnter() {
        controller.enterRoutine();
    }
}
