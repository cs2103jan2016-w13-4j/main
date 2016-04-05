// @@author A0130195M
package jfdi.logic.commands;

import jfdi.logic.events.InvalidCommandEvent;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants.CommandType;

import java.util.Optional;

/**
 * @author Liu Xinan
 */
public class InvalidCommand extends Command {

    private String inputString;
    private CommandType commandType;
    private String suggestion;

    private InvalidCommand(Builder builder) {
        this.inputString = builder.inputString;
        this.commandType = builder.commandType;
        this.suggestion = builder.suggestion;
    }

    public static class Builder {

        String inputString = "";
        CommandType commandType = null;
        String suggestion = "";

        public Builder setInputString(String inputString) {
            this.inputString = inputString;
            return this;
        }

        public Builder setCommandType(CommandType commandType) {
            this.commandType = commandType;
            return this;
        }

        public Builder setSuggestion(String suggestion) {
            this.suggestion = suggestion;
            return this;
        }

        public InvalidCommand build() {
            return new InvalidCommand(this);
        }

    }

    @Override
    public void execute() {
        // Invalid command always fail.
        setLastSuggestion(Optional.of(suggestion));
        eventBus.post(new InvalidCommandEvent(inputString, commandType, suggestion));
    }

    @Override
    public void undo() {
        assert false;
    }

}
