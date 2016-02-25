package ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainSetUp extends Application {

    private Stage primaryStage;
    private Scene scene;
    private BorderPane rootLayout;
    private AnchorPane listLayout;
    private MainController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("JFDI");

        setStageTransparent();
        loadFonts(); // if any
        initRootLayout();
        initView();

    }

    public static void main(String[] args) {
        launch(args);
    }

    /***************************
     *** LEVEL 1 Abstraction ***
     ***************************/

    private void setStageTransparent() {
        this.primaryStage.initStyle(StageStyle.TRANSPARENT);
    }

    private void loadFonts() {
        // Implement when team decides to specific font types that needs to be loaded
        // Font font1 = Font.loadFont(Main.class.getResourceAsStream(
        //                              <filepath e.g."resource/Oxygen regular.ttf">), <size>);
        // Font font2 = Font.loadFont(Main.class.getResourceAsStream(
        //                              <filepath e.g."resource/Oxygen regular.ttf">), <size>);
    }

    private void initRootLayout() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(null); // set URL of fxml file
        rootLayout = (BorderPane) loader.load();

        // Display scene with root layout
        Scene scene = new Scene(rootLayout);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
        this.scene = scene;

    }

    private void initView() throws IOException {
        // Initialize UI
        IUserInterface userInterface = new UserInterface();
        userInterface.init();

        // Load View
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(null); // set URL of fxml file
        AnchorPane appView = (AnchorPane) loader.load();
        rootLayout.setCenter(appView);

        // Initialize Controller (set up MainController Class first)
        controller = loader.getController();
        controller.setStage(primaryStage);

        // Link Controller with UI and Main (set up MainController Class first)
        controller.setUI(userInterface);
        controller.setMainApp(this);

        // Link UI with Controller
        userInterface.setController(controller);
        userInterface.displayWelcome();
    }

    /***************************
     *** LEVEL 2 Abstraction ***
     ***************************/


    /**
     * Get Methods for private variables
     */
    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    public Scene getScene() {
        return this.scene;
    }

    public BorderPane getRootLayout() {
        return this.rootLayout;
    }

    public AnchorPane getlistPane() {
        return this.listLayout;
    }

    public MainController getController() {
        return this.controller;
    }
}
