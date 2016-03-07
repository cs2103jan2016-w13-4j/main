package jfdi.ui;

public interface IUserInterface {

    void init();

    void displayWelcome();

    void processInput(String input);

    void setController(MainController controller);
}
