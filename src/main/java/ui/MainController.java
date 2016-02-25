package ui;

import javafx.stage.Stage;

public class MainController {

    public MainSetUp main;
    public IUserInterface ui;
    public Stage mainStage;

    // create a private variable commandBox

    private void initialize() {

        initList();
        initCommandPane();
        initCommandField();
        initFeedbackPane();
        initFeedbackField();

    }

    public void clearCommandBox() {
        // clear commandArea variable
    }

    public void displayFeedback(String string) {

    }

    public void clearFeedback() {

    }

    public void displayWarning() {

    }

    public void setMainApp(MainSetUp main) {
        this.main = main;
    }

    public void setUi(IUserInterface ui) {
        this.ui = ui;
    }

    public void setStage(Stage stage) {
        this.mainStage = stage;
    }

    /***************************
     *** LEVEL 1 Abstraction ***
     ***************************/

    private void initList() {

    }

    private void initCommandPane() {

    }

    private void initCommandField() {

    }

    private void initFeedbackPane() {

    }

    private void initFeedbackField() {

    }
    /***************************
     *** LEVEL 2 Abstraction ***
     ***************************/
    /***************************
     *** LEVEL 3 Abstraction ***
     ***************************/
    // Initialize the controller object
    // Deal with all UI elements, style setting, layout setting, listeners etc
}
