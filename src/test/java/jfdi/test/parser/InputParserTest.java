package jfdi.test.parser;

import jfdi.logic.commands.AddTaskCommand;
import jfdi.logic.commands.DeleteTaskCommand;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.commands.ListCommand;
import jfdi.logic.commands.RenameTaskCommand;
import jfdi.logic.commands.RescheduleTaskCommand;
import jfdi.logic.interfaces.Command;
import jfdi.parser.InputParser;
import jfdi.parser.exceptions.InvalidInputException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InputParserTest {
    InputParser parser;

    @Before
    public void setupParser() {
        parser = InputParser.getInstance();
    }

    @Test
    public void testUserInputAdd() {
        String addCommand = "add hello";
        try {
            Command command = parser.parse(addCommand);
            Assert.assertTrue(command instanceof AddTaskCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        addCommand = "hello";
        try {
            Command command = parser.parse(addCommand);
            Assert.assertTrue(command instanceof AddTaskCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        addCommand = "hello from 23/12/1993 to 26/09/1998";
        try {
            Command command = parser.parse(addCommand);
            Assert.assertTrue(command instanceof AddTaskCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        addCommand = "hello by 22nd Jan 1500hrs";
        try {
            Command command = parser.parse(addCommand);
            Assert.assertTrue(command instanceof AddTaskCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        addCommand = "hello on 15th Sep 2015";
        try {
            Command command = parser.parse(addCommand);
            Assert.assertTrue(command instanceof AddTaskCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        addCommand = "from now to tomorrow";
        try {
            Command command = parser.parse(addCommand);
            Assert.assertTrue(command instanceof InvalidCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
    }

    @Test
    public void testUserInputList() {
        String listCommand = "List";
        try {
            Command command = parser.parse(listCommand);
            Assert.assertTrue(command instanceof ListCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
        listCommand = "List all";
        try {
            Command command = parser.parse(listCommand);
            Assert.assertTrue(command instanceof ListCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
        listCommand = "list completed";
        try {
            Command command = parser.parse(listCommand);
            Assert.assertTrue(command instanceof ListCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
    }

    @Test
    public void testUserInputDelete() {
        String deleteCommand = "delete 1,2,3";
        try {
            Command command = parser.parse(deleteCommand);
            Assert.assertTrue(command instanceof DeleteTaskCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
        deleteCommand = "delete";
        try {
            Command command = parser.parse(deleteCommand);
            Assert.assertTrue(command instanceof DeleteTaskCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
    }

    @Test
    public void testUserInputRename() {
        String renameCommand = "rename 1 hello";
        try {
            Command command = parser.parse(renameCommand);
            Assert.assertTrue(command instanceof RenameTaskCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        renameCommand = "rename 10";
        try {
            Command command = parser.parse(renameCommand);
            Assert.assertTrue(command instanceof InvalidCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
    }

    @Test
    public void testUserInputReschedule() {
        String rescheduleCommand = "reschedule 4 by next week";
        try {
            Command command = parser.parse(rescheduleCommand);
            Assert.assertTrue(command instanceof RescheduleTaskCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
    }
    /*
     * @Test public void testUserInputSearch() { String searchCommand =
     * "add hello"; try { Command command = parser.parse(searchCommand);
     * Assert.assertTrue(command instanceof SearchCommand); } catch
     * (InvalidInputException e) { Assert.fail(); } }
     * @Test public void testUserInputMark() { String markCommand = "add hello";
     * try { Command command = parser.parse(markCommand);
     * Assert.assertTrue(command instanceof MarkTaskCommand); } catch
     * (InvalidInputException e) { Assert.fail(); } }
     * @Test public void testUserInputUnmark() { String unmarkCommand =
     * "add hello"; try { Command command = parser.parse(unmarkCommand);
     * Assert.assertTrue(command instanceof UnmarkTaskCommand); } catch
     * (InvalidInputException e) { Assert.fail(); } }
     * @Test public void testUserInputAlias() { String aliasCommand =
     * "add hello"; try { Command command = parser.parse(aliasCommand);
     * Assert.assertTrue(command instanceof AliasCommand); } catch
     * (InvalidInputException e) { Assert.fail(); } }
     * @Test public void testUserInputUnalias() { String unaliasCommand =
     * "add hello"; try { Command command = parser.parse(unaliasCommand);
     * Assert.assertTrue(command instanceof UnaliasCommand); } catch
     * (InvalidInputException e) { Assert.fail(); } }
     * @Test public void testUserInputDirectory() { String directoryCommand =
     * "add hello"; try { Command command = parser.parse(directoryCommand);
     * Assert.assertTrue(command instanceof DirectoryCommandStub); } catch
     * (InvalidInputException e) { Assert.fail(); } }
     * @Test public void testUserInputMove() { String moveCommand = "add hello";
     * try { Command command = parser.parse(moveCommand);
     * Assert.assertTrue(command instanceof MoveTaskCommandStub); } catch
     * (InvalidInputException e) { Assert.fail(); } }
     * @Test public void testUserInputUse() { String useCommand = "add hello";
     * try { Command command = parser.parse(useCommand);
     * Assert.assertTrue(command instanceof UseTaskCommandStub); } catch
     * (InvalidInputException e) { Assert.fail(); } }
     * @Test public void testUserInputUndo() { String undoCommand = "add hello";
     * try { Command command = parser.parse(undoCommand);
     * Assert.assertTrue(command instanceof UndoCommandStub); } catch
     * (InvalidInputException e) { Assert.fail(); } }
     * @Test public void testUserInputHelp() { String helpCommand = "add hello";
     * try { Command command = parser.parse(helpCommand);
     * Assert.assertTrue(command instanceof HelpCommandStub); } catch
     * (InvalidInputException e) { Assert.fail(); } }
     * @Test public void testUserInputWildcard() { String wildcardCommand =
     * "add hello"; try { Command command = parser.parse(wildcardCommand);
     * Assert.assertTrue(command instanceof WildcardCommandStub); } catch
     * (InvalidInputException e) { Assert.fail(); } }
     */
}
