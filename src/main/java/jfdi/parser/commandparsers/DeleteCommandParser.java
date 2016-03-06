package jfdi.parser.commandparsers;

import java.util.ArrayList;

import jfdi.logic.commands.DeleteCommandStub;
import jfdi.logic.interfaces.AbstractCommand;

public class DeleteCommandParser extends AbstractCommandParser {

    public static DeleteCommandParser instance;

    private DeleteCommandParser() {

    }

    public static DeleteCommandParser getInstance() {
        if (instance == null) {
            return instance = new DeleteCommandParser();
        }

        return instance;
    }

    /**
     * This method builds a DeleteCommand by extracting out the list of taskIDs
     * specfied for deletion by the user and passing it into the command
     * builder.
     *
     */
    @Override
    public AbstractCommand build(String input) {
        DeleteCommandStub.Builder deleteCommandBuilder = new DeleteCommandStub.Builder();
        ArrayList<String> taskIds = getTaskIds(input);
        deleteCommandBuilder.addTaskIds(taskIds);
        DeleteCommandStub deleteCommand = deleteCommandBuilder.build();
        return deleteCommand;
    }

}
