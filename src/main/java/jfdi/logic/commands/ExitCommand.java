package jfdi.logic.commands;

import jfdi.logic.events.ExitCalledEvent;
import jfdi.logic.interfaces.Command;

/**
 * @author Liu Xinan
 */
public class ExitCommand extends Command {

    private ExitCommand(Builder builder) {}

    public static class Builder {

        public ExitCommand build() {
            return new ExitCommand(this);
        }

    }

    @Override
    public void execute() {
        // Nothing needs to be done.
        // Post an event to notify UI to exit.
        eventBus.post(new ExitCalledEvent());
    }

    @Override
    public void undo() {
        throw new UnsupportedOperationException();
    }

}
