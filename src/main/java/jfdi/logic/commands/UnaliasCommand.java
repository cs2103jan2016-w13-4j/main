package jfdi.logic.commands;

import jfdi.logic.events.UnaliasDoneEvent;
import jfdi.logic.events.UnaliasFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.AliasAttributes;
import jfdi.storage.exceptions.DuplicateAliasException;
import jfdi.storage.exceptions.InvalidAliasException;
import jfdi.storage.exceptions.InvalidAliasParametersException;

/**
 * @@author Liu Xinan
 */
public class UnaliasCommand extends Command {

    private String alias;
    private String command;

    private UnaliasCommand(Builder builder) {
        this.alias = builder.alias;
    }

    public String getAlias() {
        return alias;
    }

    public static class Builder {

        String alias;

        public Builder setAlias(String alias) {
            this.alias = alias;
            return this;
        }

        public UnaliasCommand build() {
            return new UnaliasCommand(this);
        }

    }

    @Override
    public void execute() {
        try {
            command = aliasDb.getCommandFromAlias(alias);

            aliasDb.destroy(alias);
            parser.setAliases(aliasDb.getAll());

            pushToUndoStack();
            eventBus.post(new UnaliasDoneEvent(alias));
        } catch (InvalidAliasException e) {
            eventBus.post(new UnaliasFailedEvent(alias, UnaliasFailedEvent.Error.NON_EXISTENT_ALIAS));
        }
    }

    @Override
    public void undo() {
        try {
            AliasAttributes oldAlias = new AliasAttributes(alias, command);
            oldAlias.save();
            parser.setAliases(aliasDb.getAll());

            pushToRedoStack();
        } catch (InvalidAliasParametersException | DuplicateAliasException e) {
            assert false;
        }
    }

}
