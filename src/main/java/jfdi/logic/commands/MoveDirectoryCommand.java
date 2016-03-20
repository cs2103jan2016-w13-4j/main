package jfdi.logic.commands;

import jfdi.logic.events.DirectoryMovedEvent;
import jfdi.logic.events.MoveDirectoryFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.exceptions.FilesReplacedException;

/**
 * @author Xinan
 */
public class MoveDirectoryCommand extends Command {

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
        try {
            MainStorage.getInstance().changeDirectory(newDirectory);
            eventBus.post(new DirectoryMovedEvent(newDirectory));
        } catch (FilesReplacedException e) {
            eventBus.post(new MoveDirectoryFailedEvent(newDirectory, MoveDirectoryFailedEvent.Error.FILE_REPLACED,
                e.getReplacedFilePairs()));
        } catch (IllegalAccessException e) {
            eventBus.post(new MoveDirectoryFailedEvent(newDirectory, MoveDirectoryFailedEvent.Error.ACCESS_DENIED));
        }
    }

}
