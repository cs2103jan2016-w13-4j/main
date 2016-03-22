package jfdi.logic.interfaces;

import com.google.common.eventbus.EventBus;
import jfdi.ui.UI;

import java.util.Stack;

/**
 * @author Liu Xinan
 */
public abstract class Command {

    protected EventBus eventBus = UI.getEventBus();

    protected static Stack<Command> undoStack = new Stack<>();
    protected static Stack<Command> redoStack = new Stack<>();
    private static boolean redoing = false;

    /**
     * Executes the command.
     */
    public abstract void execute();

    /**
     * Undoes the command.
     */
    public abstract void undo();

    protected void setRedoing(boolean redo) {
        redoing = redo;
    }

    protected void pushToUndoStack() {
        if (!redoing) {
            while (!redoStack.empty()) {
                undoStack.push(redoStack.pop());
            }
        }

        undoStack.push(this);
    }

    protected void pushToRedoStack() {
        redoStack.push(this);
    }

}
