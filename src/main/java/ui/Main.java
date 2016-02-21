package ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
	
	private Stage _primaryStage;
	private Scene _JFDIScene;
	private BorderPane _rootLayout;
	private AnchorPane _listLayout;
	// create a private controller variable using the Controller Class

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		_primaryStage = primaryStage;
		_primaryStage.setTitle("JFDI");
		
		setStageTransparent();
		loadFonts(); // if any
		initRootLayut();
		showJFDIView();	
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * LEVEL 1 of abstraction
	 */
	
	private void setStageTransparent() {
		_primaryStage.initStyle(StageStyle.TRANSPARENT);
	}

	private void loadFonts() {
		// Implement when team decides to specific font types that needs to be loaded
		// Font font1 = Font.loadFont(Main.class.getResourceAsStream(<filepath e.g."resource/Oxygen regular.ttf">), <size>);
        // Font font2 = Font.loadFont(Main.class.getResourceAsStream(<filepath e.g."resource/Oxygen regular.ttf">), <size>);
	}	

	private void initRootLayut() throws IOException {
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(null); // set URL of fxml file
		_rootLayout = loader.load();
		
	}

	private void showJFDIView() {
		// Initialize UI
		// Load View
		// Initialize Controller
		// Link Controller with UI and Main
		// Link UI with Controller
	}
	
	/**
	 * LEVEL 2 of abstraction
	 */
	
	
	
	/**
	 * Get Methods for private variables
	 */
	public Stage getPrimaryStage() {
		return _primaryStage;
	}
	
	public Scene getJFDIScene() {
		return _JFDIScene;
	}
	
	public BorderPane getRootLayout() {
		return _rootLayout;
	}
	
	public AnchorPane getlistPane() {
		return _listLayout;
	}
	
	// create a get method for private controller
	
}
