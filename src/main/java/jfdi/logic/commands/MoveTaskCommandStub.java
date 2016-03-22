package jfdi.logic.commands;

import jfdi.logic.interfaces.Command;

public class MoveTaskCommandStub extends Command {

    private String directory;

    public MoveTaskCommandStub(Builder builder) {
        this.directory = builder.directory;
    }

    public static class Builder {
        private String directory;

        public Builder setDirectory(String dir) {
            this.directory = dir;
            return this;
        }

        public MoveTaskCommandStub build() {
            return new MoveTaskCommandStub(this);
        }
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub

    }

    @Override
    public void undo() {
        throw new UnsupportedOperationException();
    }

}
