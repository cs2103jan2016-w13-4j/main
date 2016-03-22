package jfdi.logic.commands;

import jfdi.logic.events.MoveDirectoryDoneEvent;
import jfdi.logic.events.MoveDirectoryFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.exceptions.FilesReplacedException;
import jfdi.storage.exceptions.InvalidFilePathException;

/**
 * @author Xinan
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

    public String getNewDirectory() {
        return newDirectory;
    }

    @Override
    public void execute() {
        MainStorage storage = MainStorage.getInstance();

        try {
            oldDirectory = storage.getCurrentDirectory();

            storage.changeDirectory(newDirectory);

            pushToUndoStack();
            eventBus.post(new MoveDirectoryDoneEvent(newDirectory));
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
            storage.changeDirectory(oldDirectory);

            pushToRedoStack();
        } catch (InvalidFilePathException e) {
            assert false;
        } catch (FilesReplacedException e) {
            assert false;
        }
    }
}
