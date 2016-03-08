package jfdi.logic.events;

import jfdi.parser.Constants.CommandType;

/**
 * @author Liu Xinan
 */
public class InvalidCommandEvent {

    private String inputString;
    private CommandType commandType;

    public InvalidCommandEvent(String inputString, CommandType commandType) {
        this.inputString = inputString;
        this.commandType = commandType;
    }

    public String getInputString() {
        return inputString;
    }

    public CommandType getCommandType() {
        return commandType;
    }

}
