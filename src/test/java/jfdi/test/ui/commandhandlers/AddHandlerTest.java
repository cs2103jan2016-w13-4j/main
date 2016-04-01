package jfdi.test.ui.commandhandlers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.entities.Task;
import jfdi.ui.Constants;
import jfdi.ui.items.ListItem;

public class AddHandlerTest extends CommandHandlerTest {
    
    private ObservableList<ListItem> observableList = FXCollections.observableArrayList();

    @Test
    public void addTaskDoneTest() {
        
        Task task = new Task(0, "testing1", null, null);
        TaskAttributes item = new TaskAttributes(task);
        ListItem listItem = new ListItem(1, item, true);
        observableList = null;
        observableList.add(listItem);
        
        click(cmdArea).type(KeyCode.BACK_SPACE).type(KeyCode.A).type(KeyCode.D).type(KeyCode.D).type(KeyCode.SPACE)
                .type(KeyCode.T).type(KeyCode.E).type(KeyCode.S).type(KeyCode.T).type(KeyCode.I).type(KeyCode.N)
                .type(KeyCode.G).type(KeyCode.DIGIT1).type(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals("The feedback message does not match the intended result.",
                "J.F.D.I. : " + String.format(Constants.CMD_SUCCESS_ADDED, "testing1"), fbArea.getText());
        assertEquals("The task is not properly added to the display list as an item.", observableList,
                listMain.getChildrenUnmodifiable());
        assertEquals("Notification is not updated accordingly", 1,
                incompleteCount.getText());
    }
    
    
    
    
}
