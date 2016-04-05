package jfdi.logic.commands;

import jfdi.logic.events.ShowDirectoryEvent;
import jfdi.logic.interfaces.Command;

/**
 * @@author Liu Xinan
 */
public class DirectoryCommand extends Command {

    private DirectoryCommand(Builder builder) {}

    public static class Builder {

        public DirectoryCommand build() {
            return new DirectoryCommand(this);
        }

    }

    @Override
    public void execute() {
        String pwd = mainStorage.getCurrentDirectory();
        eventBus.post(new ShowDirectoryEvent(pwd));
    }

    @Override
    public void undo() {
        assert false;
    }

}
