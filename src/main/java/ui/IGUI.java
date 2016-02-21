package ui;

import javafx.collections.ObservableMap;

public interface IGUI {
	
	public void init();
	public void displayWelcome();
	public void relayInput(String input);
	public ObservableMap<Integer, String> getJFDIList(); 
	// Change "string" to the relevant task class once created
	
	// Todo: Add a method to set a controller for the Main class (create a Controller class)	
	// e.g. public void setController(JFDIController controller);
}
