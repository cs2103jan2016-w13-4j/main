package jfdi.ui;

import jfdi.logic.ControlCenter;
import jfdi.ui.Constants.MsgType;
import jfdi.ui.commandhandlers.CommandHandler;

import com.google.common.eventbus.EventBus;

public class UI implements IUserInterface {

    private static EventBus eventBus = new EventBus();
    private static UI ourInstance = new UI();

    public CommandHandler cmdHandler = new CommandHandler();

    private MainController controller;
    private ControlCenter logic;

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

        // if (input.equalsIgnoreCase("QUIT")) {
        // System.exit(0);
        // }

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
        return controller.getIdFromIndex(onScreenId - 1);
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
        cmdHandler.setController(controller);
        eventBus.register(cmdHandler);
    }

    @Override
    public void relayToLogic(String input) {
        // Relay user input to logic and wait for reply
        logic.handleInput(input);
    }

    public static EventBus getEventBus() {
        return eventBus;
    }
}
