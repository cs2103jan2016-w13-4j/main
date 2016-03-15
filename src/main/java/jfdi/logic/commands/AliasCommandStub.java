package jfdi.logic.commands;

import jfdi.logic.interfaces.Command;

public class AliasCommandStub extends Command {

    private String alias;
    private String command;

    public AliasCommandStub(Builder builder) {
        this.alias = builder.alias;
        this.command = builder.command;
    }

    public static class Builder {
        private String alias;
        private String command;

        public Builder setAlias(String alias) {
            this.alias = alias;
            return this;
        }

        public Builder setCommand(String command) {
            this.command = command;
            return this;
        }

        public AliasCommandStub build() {
            return new AliasCommandStub(this);
        }
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub

    }

}
