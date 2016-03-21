package jfdi.test.parser;

import jfdi.logic.commands.AddTaskCommand;
import jfdi.logic.commands.AliasCommand;
import jfdi.logic.commands.DeleteTaskCommand;
import jfdi.logic.commands.DirectoryCommand;
import jfdi.logic.commands.ExitCommand;
import jfdi.logic.commands.HelpCommandStub;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.commands.ListCommand;
import jfdi.logic.commands.MarkTaskCommand;
import jfdi.logic.commands.MoveTaskCommandStub;
import jfdi.logic.commands.RenameTaskCommand;
import jfdi.logic.commands.RescheduleTaskCommand;
import jfdi.logic.commands.SearchCommand;
import jfdi.logic.commands.UnaliasCommand;
import jfdi.logic.commands.UndoCommandStub;
import jfdi.logic.commands.UnmarkTaskCommand;
import jfdi.logic.commands.UseTaskCommandStub;
import jfdi.logic.commands.WildcardCommandStub;
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

    // ==============================================================
    // Each of the below test methods represent an equivalence class
    // representing the command type under test.
    // Each test method contain tests for valid inputs as well as
    // boundary cases.
    // ==============================================================

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

        addCommand = "add";
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
        listCommand = "list voodoo";
        try {
            Command command = parser.parse(listCommand);
            Assert.assertTrue(command instanceof InvalidCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
        listCommand = "list 12345";
        try {
            Command command = parser.parse(listCommand);
            Assert.assertTrue(command instanceof InvalidCommand);
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

    @Test
    public void testUserInputSearch() {
        String searchCommand = "search hello";
        try {
            Command command = parser.parse(searchCommand);
            Assert.assertTrue(command instanceof SearchCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        searchCommand = "search";
        try {
            Command command = parser.parse(searchCommand);
            Assert.assertTrue(command instanceof InvalidCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
    }

    @Test
    public void testUserInputMark() {
        String markCommand = "mark 1";
        try {
            Command command = parser.parse(markCommand);
            Assert.assertTrue(command instanceof MarkTaskCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        markCommand = "mark";
        try {
            Command command = parser.parse(markCommand);
            Assert.assertTrue(command instanceof MarkTaskCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
    }

    @Test
    public void testUserInputUnmark() {
        String unmarkCommand = "unmark 1";
        try {
            Command command = parser.parse(unmarkCommand);
            Assert.assertTrue(command instanceof UnmarkTaskCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        unmarkCommand = "unmark dfsdfsdf";
        try {
            Command command = parser.parse(unmarkCommand);
            Assert.assertTrue(command instanceof UnmarkTaskCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
    }

    @Test
    public void testUserInputAlias() {
        String aliasCommand = "alias add hello";
        try {
            Command command = parser.parse(aliasCommand);
            Assert.assertTrue(command instanceof AliasCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        aliasCommand = "alias banana hello";
        try {
            Command command = parser.parse(aliasCommand);
            Assert.assertTrue(command instanceof AliasCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        aliasCommand = "alias hello";
        try {
            Command command = parser.parse(aliasCommand);
            Assert.assertTrue(command instanceof InvalidCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
    }

    @Test
    public void testUserInputUnalias() {
        String unaliasCommand = "unalias hello";
        try {
            Command command = parser.parse(unaliasCommand);
            Assert.assertTrue(command instanceof UnaliasCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        unaliasCommand = "unalias ";
        try {
            Command command = parser.parse(unaliasCommand);
            Assert.assertTrue(command instanceof InvalidCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
    }

    @Test
    public void testUserInputDirectory() {
        String directoryCommand = "directory";
        try {
            Command command = parser.parse(directoryCommand);
            Assert.assertTrue(command instanceof DirectoryCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        // For single word commands, add extra characters after the command
        directoryCommand = "directory to NUS";
        try {
            Command command = parser.parse(directoryCommand);
            Assert.assertTrue(command instanceof AddTaskCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
    }

    @Test
    public void testUserInputMove() {
        String moveCommand = "move";
        try {
            Command command = parser.parse(moveCommand);
            Assert.assertTrue(command instanceof InvalidCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        moveCommand = "move C:/";
        try {
            Command command = parser.parse(moveCommand);
            Assert.assertTrue(command instanceof MoveTaskCommandStub);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
    }

    @Test
    public void testUserInputUse() {
        String useCommand = "use";
        try {
            Command command = parser.parse(useCommand);
            Assert.assertTrue(command instanceof InvalidCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
        useCommand = "use C://";
        try {
            Command command = parser.parse(useCommand);
            Assert.assertTrue(command instanceof UseTaskCommandStub);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
    }

    @Test
    public void testUserInputUndo() {
        String undoCommand = "undo";
        try {
            Command command = parser.parse(undoCommand);
            Assert.assertTrue(command instanceof UndoCommandStub);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        undoCommand = "undo my life";
        try {
            Command command = parser.parse(undoCommand);
            Assert.assertTrue(command instanceof AddTaskCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
    }

    @Test
    public void testUserInputHelp() {
        String helpCommand = "help";
        try {
            Command command = parser.parse(helpCommand);
            Assert.assertTrue(command instanceof HelpCommandStub);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        helpCommand = "help me get my life back on track";
        try {
            Command command = parser.parse(helpCommand);
            Assert.assertTrue(command instanceof AddTaskCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
    }

    @Test
    public void testUserInputWildcard() {
        String wildcardCommand = "surprise";
        try {
            Command command = parser.parse(wildcardCommand);
            Assert.assertTrue(command instanceof WildcardCommandStub);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        wildcardCommand = "surprise!!";
        try {
            Command command = parser.parse(wildcardCommand);
            Assert.assertTrue(command instanceof WildcardCommandStub);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        wildcardCommand = "surprise!!!!!!!!!!!!!!";
        try {
            Command command = parser.parse(wildcardCommand);
            Assert.assertTrue(command instanceof WildcardCommandStub);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        wildcardCommand = "surprise myself on my birthday (since nobody else will :()";
        try {
            Command command = parser.parse(wildcardCommand);
            Assert.assertTrue(command instanceof AddTaskCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
    }

    @Test
    public void testUserInputExit() {
        String exitCommand = "exit";
        try {
            Command command = parser.parse(exitCommand);
            Assert.assertTrue(command instanceof ExitCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        exitCommand = "quit";
        try {
            Command command = parser.parse(exitCommand);
            Assert.assertTrue(command instanceof ExitCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        exitCommand = "quit slacking";
        try {
            Command command = parser.parse(exitCommand);
            Assert.assertTrue(command instanceof AddTaskCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }

        exitCommand = "exit on the left";
        try {
            Command command = parser.parse(exitCommand);
            Assert.assertTrue(command instanceof AddTaskCommand);
        } catch (InvalidInputException e) {
            Assert.fail();
        }
    }
}
