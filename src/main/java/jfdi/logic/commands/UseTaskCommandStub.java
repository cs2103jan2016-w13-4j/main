package jfdi.logic.commands;

import jfdi.logic.interfaces.Command;

public class UseTaskCommandStub extends Command {

    private String directory;

    public UseTaskCommandStub(Builder builder) {
        this.directory = builder.directory;
    }

    public static class Builder {
        private String directory;

        public Builder setDirectory(String dir) {
            this.directory = dir;
            return this;
        }

        public UseTaskCommandStub build() {
            return new UseTaskCommandStub(this);
        }
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub

    }

}
