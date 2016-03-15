package jfdi.logic.commands;

import jfdi.logic.interfaces.Command;

import java.util.ArrayList;
import java.util.Collection;

public class UnmarkTaskCommandStub extends Command {

    private ArrayList<Integer> taskIds;

    private UnmarkTaskCommandStub(Builder builder) {
        this.taskIds = builder.taskIds;
    }

    public static class Builder {

        ArrayList<Integer> taskIds = new ArrayList<>();

        public Builder addId(int id) {
            taskIds.add(id);
            return this;
        }

        public Builder addIds(Collection<Integer> ids) {
            taskIds.addAll(ids);
            return this;
        }

        public UnmarkTaskCommandStub build() {
            return new UnmarkTaskCommandStub(this);
        }

    }

    @Override
    public void execute() {

    }

}
