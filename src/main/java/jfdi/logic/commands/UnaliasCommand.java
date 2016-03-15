package jfdi.logic.commands;

import jfdi.logic.events.UnaliasDoneEvent;
import jfdi.logic.events.UnaliasFailEvent;
import jfdi.logic.interfaces.Command;
import jfdi.parser.InputParser;
import jfdi.storage.apis.AliasDb;
import jfdi.storage.exceptions.InvalidAliasException;

/**
 * @author Liu Xinan
 */
public class UnaliasCommand extends Command {

    private String alias;

    private UnaliasCommand(Builder builder) {
        this.alias = builder.alias;
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
        AliasDb aliasDb = AliasDb.getInstance();
        try {
            aliasDb.destroy(alias);
            InputParser.getInstance().setAliases(aliasDb.getAll());
            eventBus.post(new UnaliasDoneEvent(alias));
        } catch (InvalidAliasException e) {
            eventBus.post(new UnaliasFailEvent(alias, UnaliasFailEvent.Error.NON_EXISTENT_ALIAS));
        }
    }
}
