package jfdi.logic.interfaces;

import com.google.common.eventbus.EventBus;
import jfdi.common.utilities.JfdiLogger;
import jfdi.ui.UI;

import java.util.Optional;
import java.util.Stack;
import java.util.logging.Logger;

/**
 * @author Liu Xinan
 */
public abstract class Command {

    protected static final Logger logger = JfdiLogger.getLogger();

    protected static final Stack<Command> undoStack = new Stack<>();
    protected static final Stack<Command> redoStack = new Stack<>();

    private static boolean redoing = false;
    private static Optional<String> lastSuggestion = Optional.empty();

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

    public static void setLastSuggestion(Optional<String> suggestion) {
        lastSuggestion = suggestion;
    }

    public static Optional<String> getLastSuggestion() {
        return lastSuggestion;
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
