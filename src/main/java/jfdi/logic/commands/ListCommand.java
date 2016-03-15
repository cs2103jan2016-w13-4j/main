package jfdi.logic.commands;

import jfdi.logic.events.ListDoneEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.apis.TaskDb;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author Liu Xinan
 */
public class ListCommand extends Command {

    public enum ListType {
        ALL, COMPLETED, INCOMPLETE
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

    @Override
    public void execute() {
        ArrayList<TaskAttributes> tasks = new ArrayList<>(TaskDb.getInstance()
                .getAll());
        switch (listType) {
            case ALL:
                break;
            case COMPLETED:
                tasks = tasks.stream().filter(task -> task.isCompleted())
                        .collect(Collectors.toCollection(ArrayList::new));
                break;
            case INCOMPLETE:
                tasks = tasks.stream().filter(task -> !task.isCompleted())
                        .collect(Collectors.toCollection(ArrayList::new));
                break;
            default:
                break;
        }
        eventBus.post(new ListDoneEvent(listType, tasks));
    }

}
