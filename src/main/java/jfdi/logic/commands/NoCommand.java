package jfdi.logic.commands;

import jfdi.logic.events.NoThanksEvent;
import jfdi.logic.interfaces.Command;

/**
 * @author Xinan
 */
public class NoCommand extends Command {

    @Override
    public void execute() {
        eventBus.post(new NoThanksEvent());
    }

    @Override
    public void undo() {
        assert false;
    }
}
