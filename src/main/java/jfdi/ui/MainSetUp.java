package jfdi.ui;

import java.io.IOException;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jfdi.common.utilities.JfdiLogger;

public class MainSetUp extends Application {

    private Stage primaryStage;
    private Scene scene;
    private Parent rootLayout;
    private AnchorPane listLayout;
    private MainController controller;
    private Logger logger;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("JFDI");

        setLogger();
        loadFonts();
        initRootLayout();
        initView();

    }

    public static void main(String[] args) {
        launch(args);
    }

    /***************************
     *** LEVEL 1 Abstraction ***
     ***************************/

    private void setLogger() {
        logger = JfdiLogger.getLogger();

    }

    private void loadFonts() {

        //Font.loadFont(MainSetUp.class.getResource(Constants.URL_HAMSMITH_PATH).toExternalForm(), 12);
        //Font.loadFont(MainSetUp.class.getResource(Constants.URL_RALEWAY_PATH).toExternalForm(), 24);
    }

    private void initRootLayout() throws IOException {

        rootLayout = (BorderPane) FXMLLoader.load(getClass().getResource(Constants.URL_ROOT_PATH));
        logger.fine(String.format(Constants.LOG_FXML_PATH, "RootLayout.fxml", Constants.URL_ROOT_PATH));

        // Display scene with root layout
        Scene scene = new Scene(rootLayout);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Hammersmith+One");
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Raleway:900");
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Titillium+Web:200");
        primaryStage.show();
        primaryStage.setResizable(false);
        this.scene = scene;

    }

    private void initView() throws IOException {

        IUserInterface ui = UI.getInstance();

        // Load View
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(Constants.URL_LIST_PATH));
        listLayout = (AnchorPane) loader.load();
        logger.fine(String.format(Constants.LOG_FXML_PATH, "ListLayout.fxml", Constants.URL_LIST_PATH));

        // Initialize Controller
        controller = loader.getController();
        controller.initialize();

        // Link UI with Controller
        ui.setController(controller);
        ui.init();

        ((BorderPane) rootLayout).setCenter(listLayout);
        controller.setStage(primaryStage);

        // Link Controller with UI, MainSetUp and CommandHandler
        controller.setUi(ui);
        controller.setMainApp(this);

        //controller.importantList.removeAll(controller.importantList);

        controller.displayList();
        ui.displayWelcome();
    }

    /**
     * Get Methods for private variables
     */
    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    public Scene getScene() {
        return this.scene;
    }

    public Parent getRootLayout() {
        return this.rootLayout;
    }

    public AnchorPane getlistPane() {
        return this.listLayout;
    }

    public MainController getController() {
        return this.controller;
    }
}
