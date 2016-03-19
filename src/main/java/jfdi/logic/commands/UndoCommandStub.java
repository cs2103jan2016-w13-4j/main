package jfdi.logic.commands;

import jfdi.logic.interfaces.Command;

public class UndoCommandStub extends Command {

    public static class Builder {
        public UndoCommandStub build() {
            return new UndoCommandStub();
        }
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub

    }

}
