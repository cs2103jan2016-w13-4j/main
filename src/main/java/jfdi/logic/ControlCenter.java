package jfdi.logic;

import jfdi.logic.commands.ListCommand;
import jfdi.logic.interfaces.ILogic;
import jfdi.storage.records.Task;

import java.util.Collection;

/**
 * @author Liu Xinan
 */
public class ControlCenter implements ILogic {

    private static ControlCenter ourInstance = new ControlCenter();

    private ControlCenter() {
    }

    public static ControlCenter getInstance() {
        return ourInstance;
    }

    @Override
    public void handleInput(String input) {
        // TODO: Integrate when parser is ready.
        // Right now it is executing a ListCommand no matter what the input is.
        ListCommand.Builder builder = new ListCommand.Builder();
        builder.addTag("important");
        builder.addTag("urgent");
        ListCommand ls = builder.build();
        ls.execute();
    }

    public static void main(String[] args) {
        // An example of using ControlCenter and commands.

        ListCommand.addSuccessHook(command -> {
            Collection<Task> items = command.getItems();
            for (Task item : items) {
                System.out.printf("%d. %s\n", item.getId(), item.getDescription());
            }
        });

        ListCommand.addFailureHook(command -> {
            switch (command.getErrorType()) {
                case NON_EXISTENT_TAG:
                    break;
                case UNKNOWN:
                    System.out.println("Uh-oh");
                    break;
                default: break;
            }
        });

        ControlCenter cc = ControlCenter.getInstance();
        cc.handleInput("anything");
        // Uh-oh
        // It works!
    }
}
