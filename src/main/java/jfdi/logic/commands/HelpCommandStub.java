package jfdi.logic.commands;

import jfdi.logic.interfaces.Command;

public class HelpCommandStub extends Command {

    public static class Builder {
        public HelpCommandStub build() {
            return new HelpCommandStub();
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
