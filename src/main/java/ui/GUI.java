package ui;

import javafx.collections.ObservableMap;

public class GUI implements IGUI {

	@Override
	public void init() {
		// Initialize logic and blah blah
	}

	@Override
	public void displayWelcome() {
		// Create and display a default view
	}

	@Override
	public void relayInput(String input) {
		
		// Clear controller first
		// Relay user input to logic and wait for reply
		// Update UI according to reply from logic
	}

	@Override
	public ObservableMap<Integer, String> getJFDIList() {
		// Get the task list to display?
		return null;
	}
	
	// public void setController(JFDIController controller)

}
