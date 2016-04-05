package jfdi.logic.commands;

import jfdi.logic.events.HelpRequestedEvent;
import jfdi.logic.interfaces.Command;

/**
 * @author Liu Xinan
 */
public class HelpCommand extends Command {

    private HelpCommand(Builder builder) {}

    public static class Builder {

        public HelpCommand build() {
            return new HelpCommand(this);
        }

    }

    @Override
    public void execute() {
        eventBus.post(new HelpRequestedEvent());
    }

    @Override
    public void undo() {
        assert false;
    }
}
