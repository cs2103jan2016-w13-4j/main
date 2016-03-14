package jfdi.ui;

import jfdi.ui.CommandHandler.MsgType;

public interface IUserInterface {

    void init();

    void displayWelcome();

    void processInput(String input);

    void displayFeedback(String fb, MsgType type);

    void setController(MainController controller);

    void relayToLogic(String ctrlCmdShowlist);

    CommandHandler getCmdHandler();

    int getTaskId(int onScreenId);
}
