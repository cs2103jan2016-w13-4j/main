package jfdi.logic.commands;

import jfdi.logic.events.InvalidCommandEvent;
import jfdi.logic.interfaces.Command;

/**
 * @author Liu Xinan
 */
public class InvalidCommand extends Command {

    private String inputString;

    private InvalidCommand(Builder builder) {
        this.inputString = builder.inputString;
    }

    public static class Builder {

        String inputString = "";

        public Builder setInputString(String inputString) {
            this.inputString = inputString;
            return this;
        }

        public InvalidCommand build() {
            return new InvalidCommand(this);
        }

    }

    @Override
    public void execute() {
        // Invalid command always fail.
        eventBus.post(new InvalidCommandEvent(inputString));
    }

}
