package jfdi.logic.commands;

import jfdi.logic.events.CommandRedoneEvent;
import jfdi.logic.events.RedoFailedEvent;
import jfdi.logic.interfaces.Command;

/**
 * @@author Liu Xinan
 */
public class RedoCommand extends Command {

    private RedoCommand(Builder builder) {}

    public static class Builder {

        public RedoCommand build() {
            return new RedoCommand(this);
        }

    }

    @Override
    public void execute() {
        if (!redoStack.empty()) {
            setRedoing(true);

            Command lastRedoableCommand = redoStack.pop();
            lastRedoableCommand.execute();

            setRedoing(false);

            eventBus.post(new CommandRedoneEvent(lastRedoableCommand.getClass()));
        } else {
            eventBus.post(new RedoFailedEvent(RedoFailedEvent.Error.NONTHING_TO_REDO));
        }
    }

    @Override
    public void undo() {
        throw new UnsupportedOperationException();
    }

}
