// @@author A0130195M

package jfdi.logic.commands;

import jfdi.logic.events.InvalidCommandEvent;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants.CommandType;

/**
 * @author Liu Xinan
 */
public class InvalidCommand extends Command {

    private String inputString;
    private CommandType commandType;

    private InvalidCommand(Builder builder) {
        this.inputString = builder.inputString;
        this.commandType = builder.commandType;
    }

    public static class Builder {

        String inputString = "";
        CommandType commandType = null;

        public Builder setInputString(String inputString) {
            this.inputString = inputString;
            return this;
        }

        public Builder setCommandType(CommandType commandType) {
            this.commandType = commandType;
            return this;
        }

        public InvalidCommand build() {
            return new InvalidCommand(this);
        }

    }

    public String getInputString() {
        return inputString;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    @Override
    public void execute() {
        // Invalid command always fail.
        eventBus.post(new InvalidCommandEvent(inputString, commandType));

        logger.warning("Invalid command received: " + inputString);
    }

    @Override
    public void undo() {
        assert false;
    }

}
