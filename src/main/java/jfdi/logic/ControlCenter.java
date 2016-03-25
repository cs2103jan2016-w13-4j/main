package jfdi.logic;

import com.google.common.eventbus.EventBus;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.events.FilesReplacedEvent;
import jfdi.logic.events.InitializationFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.logic.interfaces.ILogic;
import jfdi.parser.InputParser;
import jfdi.parser.exceptions.InvalidInputException;
import jfdi.storage.apis.AliasAttributes;
import jfdi.storage.apis.AliasDb;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.exceptions.FilesReplacedException;
import jfdi.storage.exceptions.InvalidFilePathException;
import jfdi.ui.UI;

import java.util.Arrays;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author Liu Xinan
 */
public class ControlCenter implements ILogic {

    private static ControlCenter ourInstance = new ControlCenter();
    private static EventBus eventBus = UI.getEventBus();

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

    public void setParser(InputParser parser) {
        this.parser = parser;
    }

    private void initStorage() {
        // Set a list of permitted commands that can be aliased
        AliasAttributes.setCommandRegex(InputParser.getInstance().getAllCommandRegexes());
        try {
            MainStorage.getInstance().initialize();
        } catch (FilesReplacedException e) {
            eventBus.post(new FilesReplacedEvent(e.getReplacedFilePairs()));
        } catch (InvalidFilePathException e) {
            eventBus.post(new InitializationFailedEvent(InitializationFailedEvent.Error.INVALID_PATH,
                e.getPath()));
        }
    }

    private void initParser() {
        InputParser.getInstance().setAliases(AliasDb.getInstance().getAll());
    }
}
