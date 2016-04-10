// @@author A0130195M

package jfdi.logic.commands;

import jfdi.logic.events.UnaliasDoneEvent;
import jfdi.logic.events.UnaliasFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.exceptions.InvalidAliasException;

/**
 * @author Liu Xinan
 */
public class UnaliasCommand extends Command {

    private String alias;

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
            aliasDb.destroy(alias);
            parser.setAliases(aliasDb.getAll());

            pushToUndoStack();
            eventBus.post(new UnaliasDoneEvent(alias));

            logger.info("Alias deleted: " + alias);
        } catch (InvalidAliasException e) {
            eventBus.post(new UnaliasFailedEvent(alias, UnaliasFailedEvent.Error.NON_EXISTENT_ALIAS));

            logger.info("Delete alias failed: Invalid alias");
        }
    }

    @Override
    public void undo() {
        try {
            aliasDb.undestroy(alias);
            parser.setAliases(aliasDb.getAll());

            logger.info("Undo deleting alias: Adding alias " + alias + " back");
        } catch (InvalidAliasException e) {
            assert false;
        }
    }

}
