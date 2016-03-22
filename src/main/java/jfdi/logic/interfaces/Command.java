package jfdi.logic.interfaces;

import com.google.common.eventbus.EventBus;
import jfdi.ui.UI;

import java.util.Stack;

/**
 * @author Liu Xinan
 */
public abstract class Command {

    protected static final Stack<Command> undoStack = new Stack<>();
    protected static final Stack<Command> redoStack = new Stack<>();

    private static boolean redoing = false;

    protected EventBus eventBus = UI.getEventBus();

    /**
     * Executes the command.
     */
    public abstract void execute();

    /**
     * Undoes the command.
     */
    public abstract void undo();

    protected static void setRedoing(boolean redo) {
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
