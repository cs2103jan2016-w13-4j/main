package jfdi.ui;

import jfdi.logic.ControlCenter;

import com.google.common.eventbus.EventBus;

public class UI implements IUserInterface {

    private static final String UI_MESSAGE_INIT = "Initializing UI...";
    private static final String UI_MESSAGE_INITED = "Initialization Completed!";
    private static final String UI_MESSAGE_WELCOME = "J.F.D.I.: Hello! :) What can I do for you?";
    private static final String UI_MESSAGE_USERCMD = "You said: %1$s";
    private static final String UI_MESSAGE_RESPONSE = "J.F.D.I.: %1$s";
    private static final String UI_MESSAGE_WARNING = "Warning: %1$s";
    private static final String UI_MESSAGE_QUIT = "Bye Bye! See you next time! :)";

    private static EventBus eventBus = new EventBus();
    private MainController controller;

    private ControlCenter logic;

    public UI() {
        // what to do for constructor?
    }

    @Override
    public void init() {

        showToUser(UI_MESSAGE_INIT);

        // Initialize Logic
        logic = ControlCenter.getInstance();

        // Error: if fail to get data (logic issue, cannot find storage), query
        // user for filename

        showToUser(UI_MESSAGE_INITED);
    }

    @Override
    public void displayWelcome() {
        // Create and display a default view
        // controller.clearFb();
        showToUser(UI_MESSAGE_WELCOME);

        // display default list
    }

    @Override
    public void processInput(String input) {

        // Clear controller first
        controller.clearCmdArea();
        controller.clearFb();

        // Show user what the command recognized in the feedback area
        controller.displayFb(String.format(UI_MESSAGE_USERCMD, input));

        // Relay user input to logic and wait for reply
        logic.handleInput(input);

        /*
         * // Update UI according to reply from logic switch (feedback) { case
         * "QUIT": doQuit(); break; default: doUserCmd(); break; }
         */
    }

    @Override
    public void setController(MainController controller) {
        this.controller = controller;
    }

    /***************************
     *** LEVEL 1 Abstraction ***
     ***************************/

    private void showToUser(String string) {
        System.out.println(string);
    }

    private void doQuit() {
        showToUser(UI_MESSAGE_QUIT);
        System.exit(0);
    }

    private void doUserCmd() {
        CommandHandler cmdHandler = new CommandHandler();
        eventBus.register(cmdHandler);
    }

    public static EventBus getEventBus() {
        return eventBus;
    }
}
