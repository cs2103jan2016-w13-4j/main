package jfdi.logic.commands;

import jfdi.logic.events.AliasDoneEvent;
import jfdi.logic.events.AliasFailEvent;
import jfdi.logic.interfaces.Command;
import jfdi.parser.InputParser;
import jfdi.storage.apis.AliasAttributes;
import jfdi.storage.apis.AliasDb;
import jfdi.storage.exceptions.DuplicateAliasException;
import jfdi.storage.exceptions.InvalidAliasParametersException;

/**
 * @author Liu Xinan
 */
public class AliasCommand extends Command {

    private String command;
    private String alias;

    private AliasCommand(Builder builder) {
        this.command = builder.command;
        this.alias = builder.alias;
    }

    public static class Builder {

        String command;
        String alias;

        public Builder setCommand(String command) {
            this.command = command;
            return this;
        }

        public Builder setAlias(String alias) {
            this.alias = alias;
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
            InputParser.getInstance().setAliases(AliasDb.getInstance().getAll());
            eventBus.post(new AliasDoneEvent(command, alias));
        } catch (InvalidAliasParametersException e) {
            eventBus.post(new AliasFailEvent(command, alias, AliasFailEvent.Error.INVALID_PARAMETERS));
        } catch (DuplicateAliasException e) {
            eventBus.post(new AliasFailEvent(command, alias, AliasFailEvent.Error.DUPLICATED_ALIAS));
        }
    }
}