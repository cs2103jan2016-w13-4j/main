// @@author A0130195M

package jfdi.logic.commands;

import jfdi.logic.events.FilesReplacedEvent;
import jfdi.logic.events.MoveDirectoryFailedEvent;
import jfdi.logic.events.UseDirectoryDoneEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.exceptions.FilesReplacedException;
import jfdi.storage.exceptions.InvalidFilePathException;

/**
 * @author Liu Xinan
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
        try {
            oldDirectory = mainStorage.getCurrentDirectory();

            mainStorage.use(newDirectory);
            parser.setAliases(aliasDb.getAll());

            pushToUndoStack();
            eventBus.post(new UseDirectoryDoneEvent(newDirectory));
        } catch (FilesReplacedException e) {
            eventBus.post(new UseDirectoryDoneEvent(newDirectory));
            eventBus.post(new FilesReplacedEvent(newDirectory, e.getReplacedFilePairs()));
        } catch (InvalidFilePathException e) {
            eventBus.post(new MoveDirectoryFailedEvent(newDirectory, MoveDirectoryFailedEvent.Error.INVALID_PATH));
        }
    }

    @Override
    public void undo() {
        try {
            mainStorage.use(oldDirectory);
            parser.setAliases(aliasDb.getAll());

            pushToRedoStack();
        } catch (InvalidFilePathException | FilesReplacedException e) {
            assert false;
        }
    }
}
