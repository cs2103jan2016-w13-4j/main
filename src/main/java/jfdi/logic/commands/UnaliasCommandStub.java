package jfdi.logic.commands;

import jfdi.logic.interfaces.Command;

public class UnaliasCommandStub extends Command {

    private String alias;

    public UnaliasCommandStub(Builder builder) {
        this.alias = builder.alias;
    }

    public static class Builder {
        private String alias;

        public Builder setAlias(String alias) {
            this.alias = alias;
            return this;
        }

        public UnaliasCommandStub build() {
            return new UnaliasCommandStub(this);
        }
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub

    }

}
