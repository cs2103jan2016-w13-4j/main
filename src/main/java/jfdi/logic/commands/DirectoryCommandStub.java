package jfdi.logic.commands;

import jfdi.logic.interfaces.Command;

public class DirectoryCommandStub extends Command {

    public static class Builder {
        public DirectoryCommandStub build() {
            return new DirectoryCommandStub();
        }
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub

    }

}
