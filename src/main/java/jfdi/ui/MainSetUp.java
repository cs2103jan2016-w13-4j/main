package jfdi.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainSetUp extends Application {

    private Stage primaryStage;
    private Scene scene;
    private Parent rootLayout;
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
        // Font font1 = Font.loadFont(Main.class.getResourceAsStream(<filepath>), <size>);
        // Font font2 = Font.loadFont(Main.class.getResourceAsStream(<filepath>), <size>);
    }

    private void initRootLayout() throws IOException {

        rootLayout = (BorderPane) FXMLLoader.load(getClass().getResource("/ui/RootLayout.fxml"));

        // Display scene with root layout
        Scene scene = new Scene(rootLayout);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
        this.scene = scene;

    }

    private void initView() throws IOException {

        IUserInterface userInterface = new UI();
        CommandHandler cmdHandler = userInterface.getCmdHandler();

        // Load View
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ui/ListLayout.fxml"));
        listLayout = (AnchorPane) loader.load();

        // Initialize Controller
        controller = loader.getController();
        controller.initialize();

        // Link UI and CmdHandler with Controller
        cmdHandler.setController(controller);
        userInterface.setController(controller);
        userInterface.init();

        ((BorderPane) rootLayout).setCenter(listLayout);
        controller.setStage(primaryStage);

        // Link Controller with UI, MainSetUp and CommandHandler
        controller.setUi(userInterface);
        controller.setMainApp(this);
        controller.setCmdHandler(cmdHandler);

        userInterface.displayWelcome();

        /*        for (TaskAttributes task : TaskDb.getAll()) {
            try {
                TaskDb.destroy(task.getId());
            } catch (InvalidIdException e) {
                e.printStackTrace();
            }
        }*/

        //        controller.importantList.removeAll(controller.importantList);

        if (!controller.isEmptyDB()) {
            controller.displayList();
        }
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
