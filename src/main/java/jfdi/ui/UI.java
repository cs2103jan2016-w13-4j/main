package jfdi.ui;

import jfdi.logic.ControlCenter;

<<<<<<< HEAD
import com.google.common.eventbus.EventBus;

=======
>>>>>>> master
public class UI implements IUserInterface {

    private static final String UI_MESSAGE_INIT = "Initializing UI...";
    private static final String UI_MESSAGE_INITED = "Initialization Completed!";
    private static final String UI_MESSAGE_WELCOME = "J.F.D.I.: Hello! :) What can I do for you?";
    private static final String UI_MESSAGE_USERCMD = "You said: %1$s";
    //    private static final String UI_MESSAGE_RESPONSE = "J.F.D.I.: %1$s";
    //    private static final String UI_MESSAGE_WARNING = "Warning: %1$s";
    private static final String UI_MESSAGE_QUIT = "Bye Bye! See you next time! :)";

    private static EventBus eventBus = new EventBus();
<<<<<<< HEAD
    private MainController controller;

=======
    private CommandHandler cmdHandler = new CommandHandler();

    private MainController controller;
>>>>>>> master
    private ControlCenter logic;

    public UI() {
        // what to do for constructor?
    }

    @Override
    public void init() {

        showToUser(UI_MESSAGE_INIT);

        // Initialize Logic
        logic = ControlCenter.getInstance();
        this.prepareListener();

        showToUser(UI_MESSAGE_INITED);
    }

    @Override
    public void displayWelcome() {
        showToUser(UI_MESSAGE_WELCOME);
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
        controller.displayFb(String.format(UI_MESSAGE_USERCMD, input));

        // Relay user input to logic and wait for reply
        logic.handleInput(input);
<<<<<<< HEAD

        /*
         * // Update UI according to reply from logic switch (feedback) { case
         * "QUIT": doQuit(); break; default: doUserCmd(); break; }
         */
=======
>>>>>>> master
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

    private void prepareListener() {
        eventBus.register(cmdHandler);
    }

    private void doQuit() {
        showToUser(UI_MESSAGE_QUIT);
        System.exit(0);
    }

    public static EventBus getEventBus() {
        return eventBus;
    }

    public CommandHandler getCmdHandler() {
        return cmdHandler;
    }
}
