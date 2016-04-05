package jfdi.logic.commands;

import jfdi.logic.events.ListDoneEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @@author Liu Xinan
 */
public class ListCommand extends Command {

    public enum ListType {
        ALL,
        COMPLETED,
        INCOMPLETE,
        ALIASES,
        OVERDUE,
        UPCOMING
    }

    private ListType listType;

    private ListCommand(Builder builder) {
        this.listType = builder.listType;
    }

    public static class Builder {

        ListType listType;

        public Builder setListType(ListType listType) {
            this.listType = listType;
            return this;
        }

        public ListCommand build() {
            return new ListCommand(this);
        }
    }

    public ListType getListType() {
        return this.listType;
    }

    @Override
    public void execute() {
        ArrayList<TaskAttributes> tasks = taskDb.getAll().stream()
            .collect(Collectors.toCollection(ArrayList::new));

        switch (listType) {
            case ALL:
                break;
            case COMPLETED:
                tasks = tasks.stream().filter(TaskAttributes::isCompleted)
                    .collect(Collectors.toCollection(ArrayList::new));
                break;
            case INCOMPLETE:
                tasks = tasks.stream().filter(task -> !task.isCompleted())
                    .collect(Collectors.toCollection(ArrayList::new));
                break;
            case ALIASES:
                break;
            case OVERDUE:
                tasks = taskDb.getOverdue().stream()
                    .sorted()
                    .collect(Collectors.toCollection(ArrayList::new));
                break;
            case UPCOMING:
                tasks = taskDb.getUpcoming().stream()
                    .sorted()
                    .collect(Collectors.toCollection(ArrayList::new));
                break;
            default:
                break;
        }
        eventBus.post(new ListDoneEvent(listType, tasks));
    }

    @Override
    public void undo() {
        assert false;
    }

}
