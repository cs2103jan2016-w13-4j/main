package jfdi.logic.commands;

import jfdi.logic.events.AliasDoneEvent;
import jfdi.logic.events.AliasFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.parser.InputParser;
import jfdi.storage.apis.AliasAttributes;
import jfdi.storage.apis.AliasDb;
import jfdi.storage.exceptions.DuplicateAliasException;
import jfdi.storage.exceptions.InvalidAliasException;
import jfdi.storage.exceptions.InvalidAliasParametersException;

/**
 * @author Liu Xinan
 */
public class AliasCommand extends Command {

    private String command;
    private String alias;
    private boolean isValid;

    private AliasCommand(Builder builder) {
        this.command = builder.command;
        this.alias = builder.alias;
        this.isValid = builder.isValid;
    }

    public static class Builder {

        String command;
        String alias;
        boolean isValid;

        public Builder setCommand(String command) {
            this.command = command;
            return this;
        }

        public Builder setAlias(String alias) {
            this.alias = alias;
            return this;
        }

        public Builder setIsValid(boolean isValid) {
            this.isValid = isValid;
            return this;
        }

        public AliasCommand build() {
            return new AliasCommand(this);
        }

    }

    @Override
    public void execute() {
        AliasAttributes newAlias = new AliasAttributes(alias, command);
        try {
            newAlias.save();
            parser.setAliases(aliasDb.getAll());

            pushToUndoStack();
            eventBus.post(new AliasDoneEvent(command, alias));
        } catch (InvalidAliasParametersException e) {
            eventBus.post(new AliasFailedEvent(command, alias,
                AliasFailedEvent.Error.INVALID_PARAMETERS));
        } catch (DuplicateAliasException e) {
            eventBus.post(new AliasFailedEvent(command, alias,
                AliasFailedEvent.Error.DUPLICATED_ALIAS));
        }
    }

    @Override
    public void undo() {
        try {
            aliasDb.destroy(alias);
            parser.setAliases(aliasDb.getAll());

            pushToRedoStack();
        } catch (InvalidAliasException e) {
            // Should not happen
            assert false;
        }
    }

}
