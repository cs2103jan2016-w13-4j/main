package jfdi.logic.commands;

import java.util.ArrayList;
import java.util.Collection;

import jfdi.logic.interfaces.Command;

public class MarkTaskCommandStub extends Command {

    private ArrayList<Integer> taskIds;

    private MarkTaskCommandStub(Builder builder) {
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

        public MarkTaskCommandStub build() {
            return new MarkTaskCommandStub(this);
        }

    }

    @Override
    public void execute() {

    }

}
