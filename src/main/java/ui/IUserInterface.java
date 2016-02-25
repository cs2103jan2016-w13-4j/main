package ui;

import java.util.ArrayList;

import dummy.TaskDummy;

public interface IUserInterface {

    public void init();
    public void displayWelcome();
	public void processInput(String input);
	public ArrayList<TaskDummy> getList(); 
    // Change "string" to the relevant task class once created
	
	public void setController(MainController controller); // for Main class
	
}
