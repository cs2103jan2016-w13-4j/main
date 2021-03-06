// @@author A0130195M

package jfdi.logic.interfaces;

import java.util.Stack;
import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;

import jfdi.common.utilities.JfdiLogger;
import jfdi.parser.InputParser;
import jfdi.storage.apis.AliasDb;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.apis.TaskDb;
import jfdi.ui.UI;

/**
 * @author Liu Xinan
 */
public abstract class Command {

    protected static final Logger logger = JfdiLogger.getLogger();
    protected static EventBus eventBus = UI.getEventBus();

    protected static final Stack<Command> undoStack = new Stack<>();

    protected static UI ui = UI.getInstance();
    protected static InputParser parser = InputParser.getInstance();
    protected static MainStorage mainStorage = MainStorage.getInstance();
    protected static TaskDb taskDb = TaskDb.getInstance();
    protected static AliasDb aliasDb = AliasDb.getInstance();

    /**
     * Executes the command.
     */
    public abstract void execute();

    /**
     * Undoes the command.
     */
    public abstract void undo();

    public void pushToUndoStack() {
        undoStack.push(this);
    }

    //================================================================
    // List of setters and getters for testing.
    //================================================================


    public static void setUi(UI ui) {
        Command.ui = ui;
    }

    public static void setParser(InputParser parser) {
        Command.parser = parser;
    }

    public static void setMainStorage(MainStorage mainStorage) {
        Command.mainStorage = mainStorage;
    }

    public static void setTaskDb(TaskDb taskDb) {
        Command.taskDb = taskDb;
    }

    public static void setAliasDb(AliasDb aliasDb) {
        Command.aliasDb = aliasDb;
    }

    public static void setEventBus(EventBus eventBus) {
        Command.eventBus = eventBus;
    }

    public static Stack<Command> getUndoStack() {
        return undoStack;
    }

    public static void clearUndoStack() {
        undoStack.clear();
    }

    public static UI getUI() {
        return ui;
    }

    public static InputParser getParser() {
        return parser;
    }

    public static MainStorage getMainStorage() {
        return mainStorage;
    }

    public static TaskDb getTaskDb() {
        return taskDb;
    }

    public static AliasDb getAliasDb() {
        return aliasDb;
    }

    public static EventBus getEventBus() {
        return eventBus;
    }

}
