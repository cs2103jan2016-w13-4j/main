// @@author A0130195M

package jfdi.logic.commands;

import jfdi.logic.events.FilesReplacedEvent;
import jfdi.logic.events.MoveDirectoryDoneEvent;
import jfdi.logic.events.MoveDirectoryFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.exceptions.FilesReplacedException;
import jfdi.storage.exceptions.InvalidFilePathException;

/**
 * @author Liu Xinan
 */
public class MoveDirectoryCommand extends Command {

    private String oldDirectory;
    private String newDirectory;

    private MoveDirectoryCommand(Builder builder) {
        this.newDirectory = builder.newDirectory;
    }

    public static class Builder {

        String newDirectory;

        public Builder setNewDirectory(String newDirectory) {
            this.newDirectory = newDirectory;
            return this;
        }

        public MoveDirectoryCommand build() {
            return new MoveDirectoryCommand(this);
        }

    }

    public String getOldDirectory() {
        return oldDirectory;
    }

    public String getNewDirectory() {
        return newDirectory;
    }

    @Override
    public void execute() {
        try {
            oldDirectory = mainStorage.getCurrentDirectory();

            mainStorage.changeDirectory(newDirectory);

            pushToUndoStack();
            eventBus.post(new MoveDirectoryDoneEvent(newDirectory));
        } catch (FilesReplacedException e) {
            eventBus.post(new MoveDirectoryDoneEvent(newDirectory));
            eventBus.post(new FilesReplacedEvent(newDirectory, e.getReplacedFilePairs()));
        } catch (InvalidFilePathException e) {
            eventBus.post(new MoveDirectoryFailedEvent(newDirectory, MoveDirectoryFailedEvent.Error.INVALID_PATH));
        }
    }

    @Override
    public void undo() {
        try {
            mainStorage.changeDirectory(oldDirectory);

            pushToRedoStack();
        } catch (InvalidFilePathException e) {
            assert false;
        } catch (FilesReplacedException e) {
            eventBus.post(new FilesReplacedEvent(oldDirectory, e.getReplacedFilePairs()));
        }
    }
}
