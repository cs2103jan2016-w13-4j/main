// @@author A0130195M

package jfdi.logic;

import com.google.common.eventbus.EventBus;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.events.FilesReplacedEvent;
import jfdi.logic.events.InitializationFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.logic.interfaces.ILogic;
import jfdi.parser.InputParser;
import jfdi.parser.exceptions.InvalidInputException;
import jfdi.storage.apis.*;
import jfdi.storage.exceptions.FilesReplacedException;
import jfdi.storage.exceptions.InvalidFilePathException;
import jfdi.ui.UI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author Liu Xinan
 */
public class ControlCenter implements ILogic {

    private static ControlCenter ourInstance;

    private static EventBus eventBus = UI.getEventBus();

    private static MainStorage mainStorage = MainStorage.getInstance();
    private static TaskDb taskDb = TaskDb.getInstance();
    private static AliasDb aliasDb = AliasDb.getInstance();

    private static InputParser parser = InputParser.getInstance();

    private ControlCenter() {
        initStorage();
        initParser();
    }

    public static ControlCenter getInstance() {
        if (ourInstance == null) {
            ourInstance = new ControlCenter();
        }
        return ourInstance;
    }

    @Override
    public void handleInput(String input) {
        Command command;
        try {
            command = parser.parse(input);
        } catch (InvalidInputException e) {
            command = new InvalidCommand.Builder().build();
        }
        command.execute();
    }

    @Override
    public TreeSet<String> getKeywords() {
        TreeSet<String> keywords = Arrays.stream(InputParser.getInstance()
            .getAllCommandRegexes()
            .replaceAll("\\W+", " ").split("\\s+"))
            .filter(part -> part.length() > 1)
            .collect(Collectors.toCollection(TreeSet::new));

        AliasDb.getInstance()
            .getAll().stream()
            .map(AliasAttributes::getAlias)
            .forEach(keywords::add);

        return keywords;
    }

    @Override
    public ArrayList<TaskAttributes> getIncompleteTasks() {
        return taskDb.getAll().stream()
            .filter(task -> !task.isCompleted())
            .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<TaskAttributes> getCompletedTasks() {
        return taskDb.getAll().stream()
            .filter(TaskAttributes::isCompleted)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<TaskAttributes> getAllTasks() {
        return taskDb.getAll().stream()
            .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<TaskAttributes> getUpcomingTasks() {
        return taskDb.getUpcoming().stream()
            .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<TaskAttributes> getOverdueTasks() {
        return taskDb.getOverdue().stream()
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private void initStorage() {
        // Set a list of permitted commands that can be aliased
        AliasAttributes.setCommandRegex(parser.getAllCommandRegexes());
        try {
            mainStorage.initialize();
        } catch (FilesReplacedException e) {
            eventBus.post(new FilesReplacedEvent(e.getReplacedFilePairs()));
        } catch (InvalidFilePathException e) {
            eventBus.post(new InitializationFailedEvent(InitializationFailedEvent.Error.INVALID_PATH,
                e.getPath()));
        }
    }

    private void initParser() {
        parser.setAliases(aliasDb.getAll());
    }

    //================================================================
    // Methods for testing.
    //================================================================

    public static void removeInstance() {
        ourInstance = null;
    }

    public static void setParser(InputParser parser) {
        ControlCenter.parser = parser;
    }

    public static void setMainStorage(MainStorage mainStorage) {
        ControlCenter.mainStorage = mainStorage;
    }

    public static void setTaskDb(TaskDb taskDb) {
        ControlCenter.taskDb = taskDb;
    }

    public static void setAliasDb(AliasDb aliasDb) {
        ControlCenter.aliasDb = aliasDb;
    }

    public static void setEventBus(EventBus eventBus) {
        ControlCenter.eventBus = eventBus;
    }

}
