// @@author A0129538W

package jfdi.ui;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import jfdi.common.utilities.JfdiLogger;

public class MainSetUp extends Application {

    private static MainSetUp ourInstance = new MainSetUp();

    private Stage primaryStage;
    private Scene scene;
    private Parent rootLayout;
    private AnchorPane listLayout;
    private MainController controller;
    private Logger logger = JfdiLogger.getLogger();

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(Constants.PRODUCT_NAME);

        setLogo();
        loadFonts();
        initRootLayout();
        initView();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static MainSetUp getInstance() {
        return ourInstance;
    }

    /***************************
     *** LEVEL 1 Abstraction ***
     ***************************/

    /**
     * Set the logo image for the application.
     */
    private void setLogo() {
        primaryStage.getIcons().add(new Image(Constants.URL_LOGO_PATH));

        // Set Icon for OSX
        // Need to use Apple Java Extension, using reflection to load the class
        // so that JFDI is compilable
        if (System.getProperty("os.name").startsWith("Mac OS")) {
            try {
                Class util = Class.forName("com.apple.eawt.Application");
                Method getApplication = util.getMethod("getApplication", new Class[0]);
                Object application = getApplication.invoke(util);
                Class[] params = new Class[1];
                params[0] = java.awt.Image.class;
                Method setDockIconImage = util.getMethod("setDockIconImage", params);
                setDockIconImage.invoke(application,
                        new ImageIcon(UI.class.getResource(Constants.URL_LOGO_PATH)).getImage());
            } catch (Exception e) {
                logger.info("Not OS X");
            }
        }
    }

    /**
     * Load .ttf files of all fonts used in the application.
     */
    private void loadFonts() {

        Font.loadFont(MainSetUp.class.getResource("/ui/fonts/HammersmithOne.ttf").toExternalForm(), 12);
        Font.loadFont(MainSetUp.class.getResource("/ui/fonts/TitilliumWeb-Light.ttf").toExternalForm(), 24);
        Font.loadFont(MainSetUp.class.getResource("/ui/fonts/Lucida Console.ttf").toExternalForm(), 24);
    }

    /**
     * Initiates the root layout and sets the primary scene for the application.
     */
    private void initRootLayout() throws IOException {

        rootLayout = (BorderPane) FXMLLoader.load(getClass().getResource(Constants.URL_ROOT_PATH));
        logger.fine(String.format(Constants.LOG_FXML_PATH, "RootLayout.fxml", Constants.URL_ROOT_PATH));

        // Display scene with root layout
        scene = new Scene(rootLayout);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

    }

    /**
     * Initiates the visible display of the application.
     */
    private void initView() throws IOException, InterruptedException {

        IUserInterface ui = UI.getInstance();

        // Load View
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(Constants.URL_LIST_PATH));
        listLayout = (AnchorPane) loader.load();
        logger.fine(String.format(Constants.LOG_FXML_PATH, "ListLayout.fxml", Constants.URL_LIST_PATH));

        // Initialize Controller
        UI.getInstance().controller = loader.getController();
        controller = UI.getInstance().controller;
        ui.init();

        ((BorderPane) rootLayout).setCenter(listLayout);
        controller.setStage(primaryStage);

        // Link Controller with UI, MainSetUp and CommandHandler
        controller.setUi(ui);

        controller.hideOverlays();
        controller.updateNotiBubbles();
        controller.transListCmd();
        controller.switchTabSkin();
        initThread();
        ui.displayWelcome();
    }

    /**
     * Initiates the background thread for updating the notification bubbles.
     */
    private void initThread() {

        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException {
                while (true) {
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            controller.updateNotiBubbles();
                        }
                    });
                }
            }
        };

        controller.incompleteCount.textProperty().bind(controller.incompletePlaceHdr);
        controller.overdueCount.textProperty().bind(controller.overduePlaceHdr);
        controller.upcomingCount.textProperty().bind(controller.upcomingPlaceHdr);

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
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
}
