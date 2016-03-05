package jfdi.logic;

import com.google.common.eventbus.EventBus;
import dummy.DummyParser;
import jfdi.logic.interfaces.Command;
import jfdi.logic.interfaces.ILogic;
import jfdi.storage.data.TaskDb;

/**
 * @author Liu Xinan
 */
public class ControlCenter implements ILogic {

    private static ControlCenter ourInstance = new ControlCenter();

    private static final EventBus eventBus = new EventBus();

    private ControlCenter() {
        TaskDb.setFilePath("./user_data");
        TaskDb.load();
    }

    public static ControlCenter getInstance() {
        return ourInstance;
    }

    @Override
    public void handleInput(String input) {
        // TODO: Integrate when parser is ready.
        // Right now it is using a DummyParser
        DummyParser parser = DummyParser.getInstance();
        Command command = parser.parse(input);
        command.execute();
    }

    public static EventBus getEventBus() {
        return eventBus;
    }

}
