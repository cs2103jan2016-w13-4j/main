package jfdi.ui;

import com.google.common.eventbus.EventBus;

import jfdi.logic.ControlCenter;
import jfdi.ui.Constants.MsgType;

public class UI implements IUserInterface {

    private static EventBus eventBus = new EventBus();
    private CommandHandler cmdHandler = new CommandHandler();

    private MainController controller;
    private ControlCenter logic;

    @Override
    public void init() {

        // showToUser(UI_MESSAGE_INIT);

        // Initialize Logic
        logic = ControlCenter.getInstance();
        this.prepareListener();

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

        if (input.equalsIgnoreCase("QUIT")) {
            doQuit();
        }

        // Clear controller first
        controller.clearCmdArea();
        controller.clearFb();

        // Show user what the command recognized in the feedback area
        // controller.displayFb(String.format(Constants.UI_MESSAGE_USERCMD, input));

        // Relay user input to logic and wait for reply
        relayToLogic(input);
    }

    @Override
    public void displayFeedback(String fb, MsgType type) {
        switch (type) {
            case SUCCESS:
                controller.clearFb();
                showToUser(String.format(Constants.UI_MESSAGE_RESPONSE, fb));
                break;
            case WARNING:
                controller.clearFb();
                showToUser(String.format(Constants.UI_MESSAGE_WARNING, fb));
                break;
            case ERROR:
                controller.clearFb();
                showToUser(String.format(Constants.UI_MESSAGE_ERROR, fb));
                break;
            case EXIT:
                controller.clearFb();
                showToUser(Constants.UI_MESSAGE_QUIT);
                break;
            default:
                break;
        }

    }

    @Override
    public int getTaskId(int onScreenId) {
        return controller.indexMapId.get(onScreenId - 1);
    }

    @Override
    public void setController(MainController controller) {
        this.controller = controller;
    }

    /***************************
     *** LEVEL 1 Abstraction ***
     ***************************/

    private void showToUser(String string) {
        controller.displayFb(string);

        System.out.println(string);
    }

    private void prepareListener() {
        eventBus.register(cmdHandler);
    }

    @Override
    public void relayToLogic(String input) {
        // Relay user input to logic and wait for reply
        logic.handleInput(input);
    }

    private void doQuit() {
        showToUser(Constants.UI_MESSAGE_QUIT);
        System.exit(0);
    }

    public static EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public CommandHandler getCmdHandler() {
        return cmdHandler;
    }
}
