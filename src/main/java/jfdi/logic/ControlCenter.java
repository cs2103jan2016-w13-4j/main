package jfdi.logic;

import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.interfaces.Command;
import jfdi.logic.interfaces.ILogic;
import jfdi.parser.InputParser;
import jfdi.parser.exceptions.InvalidInputException;
import jfdi.storage.apis.AliasAttributes;
import jfdi.storage.apis.AliasDb;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.exceptions.FilesReplacedException;

/**
 * @author Liu Xinan
 */
public class ControlCenter implements ILogic {

    private static ControlCenter ourInstance = new ControlCenter();

    private ControlCenter() {
        initStorage();
        initParser();
    }

    public static ControlCenter getInstance() {
        return ourInstance;
    }

    @Override
    public void handleInput(String input) {
        InputParser parser = InputParser.getInstance();
        Command command;
        try {
            command = parser.parse(input);
        } catch (InvalidInputException e) {
            command = new InvalidCommand.Builder().build();
        }
        command.execute();
    }

    private void initStorage() {
        // Set a list of permitted commands that can be aliased
        AliasAttributes.setCommandRegex(InputParser.getInstance().getAllCommandRegexes());
        try {
            MainStorage.getInstance().initialize();
        } catch (FilesReplacedException e) {
            e.printStackTrace();
        }
    }

    private void initParser() {
        InputParser.getInstance().setAliases(AliasDb.getInstance().getAll());
    }
}
