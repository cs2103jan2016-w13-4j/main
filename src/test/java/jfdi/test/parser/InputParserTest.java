// @@author A0127393B

package jfdi.test.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jfdi.logic.commands.AddTaskCommand;
import jfdi.logic.commands.AliasCommand;
import jfdi.logic.commands.DeleteTaskCommand;
import jfdi.logic.commands.DirectoryCommand;
import jfdi.logic.commands.ExitCommand;
import jfdi.logic.commands.HelpCommand;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.commands.ListCommand;
import jfdi.logic.commands.MarkTaskCommand;
import jfdi.logic.commands.MoveDirectoryCommand;
import jfdi.logic.commands.RenameTaskCommand;
import jfdi.logic.commands.RescheduleTaskCommand;
import jfdi.logic.commands.SearchCommand;
import jfdi.logic.commands.UnaliasCommand;
import jfdi.logic.commands.UndoCommand;
import jfdi.logic.commands.UnmarkTaskCommand;
import jfdi.logic.commands.UseDirectoryCommand;
import jfdi.logic.commands.WildcardCommand;
import jfdi.logic.interfaces.Command;
import jfdi.parser.Constants;
import jfdi.parser.InputParser;
import jfdi.parser.exceptions.InvalidInputException;
import jfdi.storage.apis.AliasAttributes;
import jfdi.storage.entities.Alias;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InputParserTest {
    InputParser parser;
    ArrayList<AliasAttributes> aliasAttributes = new ArrayList<>();

    @Before
    public void setupParser() {
        parser = InputParser.getInstance();
        aliasAttributes.clear();
        aliasAttributes.add(new AliasAttributes(new Alias("ad", "add")));
        aliasAttributes.add(new AliasAttributes(new Alias("del", "delete")));
        aliasAttributes.add(new AliasAttributes(new Alias("res", "reschedule")));
        aliasAttributes.add(new AliasAttributes(new Alias("q", "quit")));
        aliasAttributes.add(new AliasAttributes(new Alias("un", "undo")));
        aliasAttributes.add(new AliasAttributes(new Alias("rm", "delete")));
        aliasAttributes.add(new AliasAttributes(new Alias("dir", "directory")));

        parser.setAliases(aliasAttributes);
    }

    // ==============================================================
    // Each of the below test methods represent an equivalence class
    // representing the command type under test.
    // Each test method contain tests for valid inputs as well as
    // boundary cases.
    // ==============================================================

    @Test
    public void testUserInputAdd() throws InvalidInputException {
        String addCommand = "add hello";
        Command command = parser.parse(addCommand);
        Assert.assertTrue(command instanceof AddTaskCommand);

        addCommand = "hello";
        command = parser.parse(addCommand);
        Assert.assertTrue(command instanceof AddTaskCommand);

        addCommand = "hello 23/12/1993 to 26/09/1998";
        command = parser.parse(addCommand);
        Assert.assertTrue(command instanceof AddTaskCommand);

        addCommand = "hello by 22nd Jan 1500hrs";
        command = parser.parse(addCommand);
        Assert.assertTrue(command instanceof AddTaskCommand);

        // With alias
        addCommand = "ad hello on 15th Sep 2015";
        command = parser.parse(addCommand);
        Assert.assertTrue(command instanceof AddTaskCommand);

        addCommand = "from now to tomorrow";
        command = parser.parse(addCommand);
        Assert.assertTrue(command instanceof InvalidCommand);

        addCommand = "add";
        command = parser.parse(addCommand);
        Assert.assertTrue(command instanceof InvalidCommand);
    }

    @Test
    public void testUserInputList() throws InvalidInputException {
        String listCommand = "List";
        Command command = parser.parse(listCommand);
        Assert.assertTrue(command instanceof ListCommand);

        listCommand = "List all";
        command = parser.parse(listCommand);
        Assert.assertTrue(command instanceof ListCommand);

        listCommand = "list completed";
        command = parser.parse(listCommand);
        Assert.assertTrue(command instanceof ListCommand);

        listCommand = "list voodoo";
        command = parser.parse(listCommand);
        Assert.assertTrue(command instanceof InvalidCommand);

        listCommand = "list 12345";
        command = parser.parse(listCommand);
        Assert.assertTrue(command instanceof InvalidCommand);

    }

    @Test
    public void testUserInputDelete() throws InvalidInputException {
        String deleteCommand = "delete 1,2,3";

        Command command = parser.parse(deleteCommand);
        Assert.assertTrue(command instanceof DeleteTaskCommand);

        deleteCommand = "delete";
        command = parser.parse(deleteCommand);
        Assert.assertTrue(command instanceof InvalidCommand);
    }

    @Test
    public void testUserInputRename() throws InvalidInputException {
        String renameCommand = "rename 1 hello";

        Command command = parser.parse(renameCommand);
        Assert.assertTrue(command instanceof RenameTaskCommand);

        renameCommand = "rename 10";

        command = parser.parse(renameCommand);
        Assert.assertTrue(command instanceof InvalidCommand);
    }

    @Test
    public void testUserInputReschedule() throws InvalidInputException {
        String rescheduleCommand = "reschedule 4 by next week";

        Command command = parser.parse(rescheduleCommand);
        Assert.assertTrue(command instanceof RescheduleTaskCommand);
    }

    @Test
    public void testUserInputSearch() throws InvalidInputException {
        String searchCommand = "search hello";

        Command command = parser.parse(searchCommand);
        Assert.assertTrue(command instanceof SearchCommand);

        searchCommand = "search";

        command = parser.parse(searchCommand);
        Assert.assertTrue(command instanceof InvalidCommand);
    }

    @Test
    public void testUserInputMark() throws InvalidInputException {
        String markCommand = "mark 1";

        Command command = parser.parse(markCommand);
        Assert.assertTrue(command instanceof MarkTaskCommand);

        markCommand = "mark";

        command = parser.parse(markCommand);
        Assert.assertTrue(command instanceof InvalidCommand);
    }

    @Test
    public void testUserInputUnmark() throws InvalidInputException {
        String unmarkCommand = "unmark 1";

        Command command = parser.parse(unmarkCommand);
        Assert.assertTrue(command instanceof UnmarkTaskCommand);

        unmarkCommand = "unmark dfsdfsdf";

        command = parser.parse(unmarkCommand);
        Assert.assertTrue(command instanceof InvalidCommand);
    }

    @Test
    public void testUserInputAlias() throws InvalidInputException {
        String aliasCommand = "alias add hello";

        Command command = parser.parse(aliasCommand);
        Assert.assertTrue(command instanceof AliasCommand);

        aliasCommand = "alias banana hello";

        command = parser.parse(aliasCommand);
        Assert.assertTrue(command instanceof AliasCommand);

        aliasCommand = "alias hello";

        command = parser.parse(aliasCommand);
        Assert.assertTrue(command instanceof InvalidCommand);
    }

    @Test
    public void testUserInputUnalias() throws InvalidInputException {
        String unaliasCommand = "unalias hello";

        Command command = parser.parse(unaliasCommand);
        Assert.assertTrue(command instanceof UnaliasCommand);

        unaliasCommand = "unalias ";

        command = parser.parse(unaliasCommand);
        Assert.assertTrue(command instanceof InvalidCommand);
    }

    @Test
    public void testUserInputDirectory() throws InvalidInputException {
        String directoryCommand = "directory";

        Command command = parser.parse(directoryCommand);
        Assert.assertTrue(command instanceof DirectoryCommand);

        // For single word commands, add extra characters after the command
        directoryCommand = "directory to NUS";

        command = parser.parse(directoryCommand);
        Assert.assertTrue(command instanceof AddTaskCommand);
    }

    @Test
    public void testUserInputMove() throws InvalidInputException {
        String moveCommand = "move";

        Command command = parser.parse(moveCommand);
        Assert.assertTrue(command instanceof InvalidCommand);

        moveCommand = "move C:/";

        command = parser.parse(moveCommand);
        Assert.assertTrue(command instanceof MoveDirectoryCommand);
    }

    @Test
    public void testUserInputUse() throws InvalidInputException {
        String useCommand = "use";

        Command command = parser.parse(useCommand);
        Assert.assertTrue(command instanceof InvalidCommand);
        useCommand = "use C://";

        command = parser.parse(useCommand);
        Assert.assertTrue(command instanceof UseDirectoryCommand);
    }

    @Test
    public void testUserInputUndo() throws InvalidInputException {
        String undoCommand = "undo";

        Command command = parser.parse(undoCommand);
        Assert.assertTrue(command instanceof UndoCommand);

        undoCommand = "undo my life";

        command = parser.parse(undoCommand);
        Assert.assertTrue(command instanceof AddTaskCommand);
    }

    @Test
    public void testUserInputHelp() throws InvalidInputException {
        String helpCommand = "help";

        Command command = parser.parse(helpCommand);
        Assert.assertTrue(command instanceof HelpCommand);

        helpCommand = "help me get my life back on track";

        command = parser.parse(helpCommand);
        Assert.assertTrue(command instanceof AddTaskCommand);
    }

    @Test
    public void testUserInputWildcard() throws InvalidInputException {
        String wildcardCommand = "surprise";
        Command command = parser.parse(wildcardCommand);
        Assert.assertTrue(command instanceof WildcardCommand);

        wildcardCommand = "surprise!!";
        command = parser.parse(wildcardCommand);
        Assert.assertTrue(command instanceof WildcardCommand);

        wildcardCommand = "surprise!!!!!!!!!!!!!!";
        command = parser.parse(wildcardCommand);
        Assert.assertTrue(command instanceof WildcardCommand);

        wildcardCommand = "surprise myself on my birthday (since nobody else will :()";
        command = parser.parse(wildcardCommand);
        Assert.assertTrue(command instanceof AddTaskCommand);
    }

    @Test
    public void testUserInputExit() throws InvalidInputException {
        String exitCommand = "exit";

        Command command = parser.parse(exitCommand);
        Assert.assertTrue(command instanceof ExitCommand);

        exitCommand = "quit";

        command = parser.parse(exitCommand);
        Assert.assertTrue(command instanceof ExitCommand);

        exitCommand = "quit slacking";

        command = parser.parse(exitCommand);
        Assert.assertTrue(command instanceof AddTaskCommand);

        exitCommand = "exit on the left";

        command = parser.parse(exitCommand);
        Assert.assertTrue(command instanceof AddTaskCommand);
    }

    @Test
    public void testInvalid() throws InvalidInputException {

        Command command = null;
        try {
            command = parser.parse(null);
        } catch (InvalidInputException e) {
            Assert.assertTrue(true);
        }

        try {
            command = parser.parse("");
        } catch (InvalidInputException e) {
            Assert.assertTrue(true);
        }

        try {
            command = parser.parse(" ");
        } catch (InvalidInputException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testSetAliases() {

        List<String> aliasesInAliasAttributes =
            Arrays.asList(aliasAttributes.stream().map(aliasAttribute -> aliasAttribute.getAlias())
                .toArray(size -> new String[size]));
        List<String> commandsInAliasAttributes =
            Arrays.asList(aliasAttributes.stream().map(aliasAttribute -> aliasAttribute.getCommand())
                .toArray(size -> new String[size]));

        for (String alias : parser.getAliasMap().keySet()) {
            Assert.assertTrue(aliasesInAliasAttributes.contains(alias));
            String command = commandsInAliasAttributes.get(aliasesInAliasAttributes.indexOf(alias));
            Assert.assertTrue(parser.getAliasMap().get(alias).equals(command));
        }

        for (String alias : aliasesInAliasAttributes) {
            Assert.assertTrue(parser.getAliasMap().keySet().contains(alias));
            String command = parser.getAliasMap().get(alias);
            Assert.assertTrue(commandsInAliasAttributes.get(aliasesInAliasAttributes.indexOf(alias)).equals(command));
        }
    }

    @Test
    public void testGetAllCommandRegexes() {
        String allCommandRegexes = parser.getAllCommandRegexes();
        Assert.assertTrue(allCommandRegexes.equals(String.join("|", Constants.getCommandRegexes())));
    }
}
