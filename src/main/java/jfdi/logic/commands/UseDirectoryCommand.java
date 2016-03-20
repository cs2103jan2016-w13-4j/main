package jfdi.logic.commands;

import jfdi.logic.events.MoveDirectoryFailedEvent;
import jfdi.logic.events.UseDirectoryDoneEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.exceptions.FilesReplacedException;
import jfdi.storage.exceptions.InvalidFilePathException;

/**
 * @author Xinan
 */
public class UseDirectoryCommand extends Command {

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
            MainStorage.getInstance().use(newDirectory);
            eventBus.post(new UseDirectoryDoneEvent(newDirectory));
        } catch (FilesReplacedException e) {
            eventBus.post(new MoveDirectoryFailedEvent(newDirectory, MoveDirectoryFailedEvent.Error.FILE_REPLACED,
                e.getReplacedFilePairs()));
        } catch (InvalidFilePathException e) {
            eventBus.post(new MoveDirectoryFailedEvent(newDirectory, MoveDirectoryFailedEvent.Error.INVALID_PATH));
        }
    }

}
