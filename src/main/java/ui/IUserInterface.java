package ui;

import java.util.ArrayList;

import dummy.TaskDummy;

public interface IUserInterface {

    void init();
    void displayWelcome();
    void processInput(String input);
    ArrayList<TaskDummy> getList();
    // Change "string" to the relevant task class once created
    void setController(MainController controller);
}
