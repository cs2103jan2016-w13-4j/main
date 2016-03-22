package jfdi.logic.events;

import jfdi.logic.interfaces.Command;

/**
 * @author Xinan
 */
public class CommandRedoneEvent {

    private Class<? extends Command> commandType;

    public CommandRedoneEvent(Class<? extends Command> commandType) {
        this.commandType = commandType;
    }

    public Class<? extends Command> getCommandType() {
        return commandType;
    }

}
