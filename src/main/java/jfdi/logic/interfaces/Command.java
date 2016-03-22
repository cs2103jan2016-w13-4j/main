package jfdi.logic.interfaces;

import com.google.common.eventbus.EventBus;
import jfdi.ui.UI;

import java.util.Stack;

/**
 * @author Liu Xinan
 */
public abstract class Command {

    protected EventBus eventBus = UI.getEventBus();

    private static Stack<Command> undoStack = new Stack<>();
    private static Stack<Command> redoStack = new Stack<>();

    /**
     * Executes the command.
     */
    public abstract void execute();

    /**
     * Undoes the command.
     */
    protected void undo() {
        throw new UnsupportedOperationException(this.getClass().getName() + " is not undoable!");
    }

    protected void pushToUndoStack() {
        undoStack.push(this);
    }

    protected void pushToRedoStack() {
        redoStack.push(this);
    }

}
