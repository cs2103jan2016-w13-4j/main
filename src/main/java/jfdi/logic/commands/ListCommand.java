package jfdi.logic.commands;

import java.util.ArrayList;
import java.util.Collection;

import jfdi.logic.events.ListDoneEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.data.TaskAttributes;
import jfdi.storage.data.TaskDb;

/**
 * @author Liu Xinan
 */
public class ListCommand extends Command {

    private ArrayList<String> tags;
    private ArrayList<TaskAttributes> items;

    private ListCommand(Builder builder) {
        this.tags = builder.tags;
        this.items = new ArrayList<>();
    }

    public static class Builder {

        ArrayList<String> tags = new ArrayList<>();

        public Builder addTag(String tag) {
            this.tags.add(tag);
            return this;
        }

        public Builder addTags(Collection<String> tags) {
            this.tags.addAll(tags);
            return this;
        }

        public ListCommand build() {
            return new ListCommand(this);
        }
    }


    /**
     * Get the tags requested in the input.
     *
     * @return List of tags
     */
    public ArrayList<String> getTags() {
        return tags;
    }

    @Override
    public void execute() {
        if (tags.isEmpty()) {
            items.addAll(TaskDb.getAll());
        } else {
            for (String tag : tags) {
                items.addAll(TaskDb.getByTag(tag));
            }
        }
        eventBus.post(new ListDoneEvent(tags, items));
    }

}
