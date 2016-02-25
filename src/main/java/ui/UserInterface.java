package ui;

import java.util.ArrayList;

import dummy.LogicDummy;
import dummy.LogicInterfaceDummy;
import dummy.TaskDummy;

public class UserInterface implements IUserInterface {

    private static final String UI_MESSAGE_INIT = "Initializing UI...";
    private static final String UI_MESSAGE_INITED = "Initialization Completed!";
    private static final String UI_MESSAGE_WELCOME = "Welcome to JFDI! :) "
            + "What would you like to do now?";
    private static final String UI_MESSAGE_USERCMD = "Your input: %1$s";
    private static final String UI_MESSAGE_QUIT = "Bye Bye! See you next time! :)";

    LogicInterfaceDummy logic;
    private MainController controller;

    public UserInterface() {
        // what to do for constructor?
    }

    @Override
    public void init() {

        showToUser(UI_MESSAGE_INIT);

        // Initialize Logic
        logic = new LogicDummy();
        logic.init();

        // Error: if fail to get data (logic issue), query user for filename

        showToUser(UI_MESSAGE_INITED);
    }

    @Override
    public void displayWelcome() {
        // Create and display a default view
        // display default list

        controller.clearFeedback();
        controller.displayFeedback(UI_MESSAGE_WELCOME);
    }

    @Override
    public void processInput(String input) {

        // Clear controller first
        controller.clearCommandBox();
        controller.clearFeedback();

        // Show user the command recognized in the feedback area
        controller.displayFeedback(String.format(UI_MESSAGE_USERCMD, input));
        // Relay user input to logic and wait for reply
        String feedback = logic.handleInput(input);

        // Update UI according to reply from logic
        switch (feedback) {
            case "QUIT":
                doQuit();
                break;
            default:
                doUserCmd();
                break;
        }
    }

    @Override
    public ArrayList<TaskDummy> getList() {
        // get list of tasks from storage if command is completed (ArrayList<Task>)
        return null;
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

    }
}
