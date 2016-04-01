package jfdi.ui;

import jfdi.ui.Constants.MsgType;

public interface IUserInterface {

    void init();

    void displayWelcome();

    void processInput(String input);

    void relayToLogic(String ctrlCmdShowlist);

    int getTaskId(int onScreenId);

    void displayFeedback(String fb, MsgType type);

    void appendFeedback(String fb, MsgType type);
}
