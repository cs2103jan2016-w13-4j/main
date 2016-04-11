// @@author A0130195M

package jfdi.logic.commands;

import jfdi.logic.events.CommandUndoneEvent;
import jfdi.logic.events.UndoFailedEvent;
import jfdi.logic.interfaces.Command;

/**
 * @author Liu Xinan
 */
public class UndoCommand extends Command {

    private UndoCommand(Builder builder) {}

    public static class Builder {

        public UndoCommand build() {
            return new UndoCommand(this);
        }

    }

    @Override
    public void execute() {
        if (!undoStack.empty()) {
            Command lastUndoableCommand = undoStack.pop();
            lastUndoableCommand.undo();

            eventBus.post(new CommandUndoneEvent(lastUndoableCommand.getClass()));

            logger.info("Undoing previous " + lastUndoableCommand.getClass());
        } else {
            eventBus.post(new UndoFailedEvent(UndoFailedEvent.Error.NONTHING_TO_UNDO));

            logger.warning("Undo failed: Nothing to undo");
        }
    }

    @Override
    public void undo() {
        assert false;
    }

}
