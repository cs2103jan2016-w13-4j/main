// @@author A0129538W

package jfdi.ui;

import jfdi.ui.Constants.MsgType;

public interface IUserInterface {

    /**
     * Initiates connections with the Logic component.
     */
    void init();

    /**
     * Displays welcome on display after setup.
     */
    void displayWelcome();

    /**
     * Processes user input in the command area.
     * @param input
     *             user input string
     */
    void processInput(String input);

    /**
     * Relays the internal commands to the Logic component
     * for the purpose of refreshing on-screen display.
     * @param ctrlCmdShowlist
     *                      internal command string
     */
    void relayToLogic(String CmdShowlist);

    /**
     * Retrieves the task storage ID given a specific on-screen
     * index.
     * @param onScreenId
     *                  the on-screen index of a task
     * @return the storage ID of the task
     */
    int getTaskId(int onScreenId);

    /**
     * Displays the feedback message according to the specified
     * message type (and clears the feedback area).
     * @param fb
     *          the message to be displayed
     * @param type
     *          the type of the message to be displayed
     */
    void displayFeedback(String fb, MsgType type);

    /**
     * Displays the feedback message according to the specified
     * message type (without clearing the feedback area)
     * @param fb
     *          the message to be displayed
     * @param type
     *          the type of the message to be displayed
     */
    void appendFeedback(String fb, MsgType type);
}
