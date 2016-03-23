package jfdi.logic.commands;

import jfdi.logic.events.MoveDirectoryFailedEvent;
import jfdi.logic.events.UseDirectoryDoneEvent;
import jfdi.logic.interfaces.Command;
import jfdi.parser.InputParser;
import jfdi.storage.apis.AliasDb;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.exceptions.FilesReplacedException;
import jfdi.storage.exceptions.InvalidFilePathException;

/**
 * @author Xinan
 */
public class UseDirectoryCommand extends Command {

    private String oldDirectory;
    private String newDirectory;

    private UseDirectoryCommand(Builder builder) {
        this.newDirectory = builder.newDirectory;
    }

    public static class Builder {

        String newDirectory;

        public Builder setNewDirectory(String newDirectory) {
            this.newDirectory = newDirectory;
            return this;
        }

        public UseDirectoryCommand build() {
            return new UseDirectoryCommand(this);
        }

    }

    public String getNewDirectory() {
        return newDirectory;
    }

    @Override
    public void execute() {
        MainStorage storage = MainStorage.getInstance();
        try {
            oldDirectory = storage.getCurrentDirectory();

            storage.use(newDirectory);
            InputParser.getInstance().setAliases(AliasDb.getInstance().getAll());

            pushToUndoStack();
            eventBus.post(new UseDirectoryDoneEvent(newDirectory));
        } catch (FilesReplacedException e) {
            eventBus.post(new MoveDirectoryFailedEvent(newDirectory, MoveDirectoryFailedEvent.Error.FILE_REPLACED,
                e.getReplacedFilePairs()));
        } catch (InvalidFilePathException e) {
            eventBus.post(new MoveDirectoryFailedEvent(newDirectory, MoveDirectoryFailedEvent.Error.INVALID_PATH));
        }
    }

    @Override
    public void undo() {
        MainStorage storage = MainStorage.getInstance();
        try {
            storage.use(oldDirectory);
            InputParser.getInstance().setAliases(AliasDb.getInstance().getAll());

            pushToRedoStack();
        } catch (InvalidFilePathException | FilesReplacedException e) {
            assert false;
        }
    }
}
