package jfdi.parser.commandparsers;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jfdi.logic.commands.DeleteCommandStub;
import jfdi.logic.interfaces.AbstractCommand;
import jfdi.parser.Constants;

public class DeleteCommandParser extends CommandParser {

    public static DeleteCommandParser instance;

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
     */
    @Override
    public AbstractCommand build(String input) {
        DeleteCommandStub.Builder deleteCommandBuilder = new DeleteCommandStub.Builder();
        ArrayList<String> taskIds = getTaskIds(input);
        deleteCommandBuilder.addTaskIds(taskIds);
        DeleteCommandStub deleteCommand = deleteCommandBuilder.build();
        return deleteCommand;
    }

    private ArrayList<String> getTaskIds(String input) {
        Pattern pattern = Pattern.compile(Constants.REGEX_TASKID);
        Matcher matcher = pattern.matcher(input);
        ArrayList<String> taskIds = new ArrayList<>();

        while (matcher.find()) {
            String taskId = getTrimmedSubstringInRange(input, matcher.start(),
                    matcher.end());
            taskIds.add(taskId);
        }

        return taskIds;
    }
}
