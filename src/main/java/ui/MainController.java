package ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainController {

    public MainSetUp main;
    public IUserInterface ui;
    public Stage mainStage;

    // create a private variable commandBox

    @FXML
    public TextField dayDisplayer;
    @FXML
    private ListView<String> listBoxMain;
    @FXML
    private TextField txtAddItem;

    public void initialize() {

        initDate();
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

    public void initDate() {

        System.out.println("Hello!");
        dayDisplayer = new TextField();
        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        Calendar cal = Calendar.getInstance();
        dayDisplayer.setText(dateFormat.format(cal.getTime()));
    }

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
