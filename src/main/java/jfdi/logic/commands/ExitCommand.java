package jfdi.logic.commands;

import jfdi.CoverageIgnore;
import jfdi.logic.events.ExitCalledEvent;
import jfdi.logic.interfaces.Command;

import java.util.ArrayList;
import java.util.function.Consumer;

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

}
