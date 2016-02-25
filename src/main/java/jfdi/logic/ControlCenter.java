package jfdi.logic;

import dummy.DummyParser;
import jfdi.logic.interfaces.AbstractCommand;
import jfdi.logic.interfaces.ILogic;

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
        // Right now it is using a DummyParser
        DummyParser parser = DummyParser.getInstance();
        AbstractCommand command = parser.parse(input);
        command.execute();
    }

}
