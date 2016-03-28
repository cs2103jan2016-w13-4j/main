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
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author Liu Xinan
 */
public class ControlCenter implements ILogic {

    private static ControlCenter ourInstance = new ControlCenter();

    private final EventBus eventBus = UI.getEventBus();

    private MainStorage mainStorage = MainStorage.getInstance();
    private TaskDb taskDb = TaskDb.getInstance();
    private AliasDb aliasDb = AliasDb.getInstance();

    private InputParser parser = InputParser.getInstance();

    private ControlCenter() {
        initStorage();
        initParser();
    }

    public static ControlCenter getInstance() {
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

        if (!(command instanceof InvalidCommand)) {
            Command.setLastSuggestion(Optional.empty());
        }
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
    // List of setters for testing.
    //================================================================

    public void setParser(InputParser parser) {
        this.parser = parser;
    }

    public void setMainStorage(MainStorage mainStorage) {
        this.mainStorage = mainStorage;
    }

    public void setTaskDb(TaskDb taskDb) {
        this.taskDb = taskDb;
    }

    public void setAliasDb(AliasDb aliasDb) {
        this.aliasDb = aliasDb;
    }
}
