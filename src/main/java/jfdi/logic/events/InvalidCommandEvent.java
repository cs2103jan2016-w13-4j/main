package jfdi.logic.events;

import jfdi.parser.Constants.CommandType;

/**
 * @author Liu Xinan
 */
public class InvalidCommandEvent {

    private String inputString;
    private CommandType commandType;
    private String suggestion;

    public InvalidCommandEvent(String inputString, CommandType commandType, String suggestion) {
        this.inputString = inputString;
        this.commandType = commandType;
        this.suggestion = suggestion;
    }

    public String getInputString() {
        return inputString;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getSuggestion() {
        return suggestion;
    }

}
