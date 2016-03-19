package jfdi.logic.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

import jfdi.logic.events.SearchDoneEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.apis.TaskDb;

/**
 * @author Liu Xinan
 */
public class SearchCommand extends Command {

    private HashSet<String> keywords;

    private SearchCommand(Builder builder) {
        this.keywords = builder.keywords;
    }

    public static class Builder {

        HashSet<String> keywords = new HashSet<>();

        public Builder addKeyword(String keyword) {
            this.keywords.add(keyword);
            return this;
        }

        public Builder addKeywords(Collection<String> keywords) {
            this.keywords.addAll(keywords);
            return this;
        }

        public SearchCommand build() {
            return new SearchCommand(this);
        }

    }

    @Override
    public void execute() {
        Collection<TaskAttributes> allTasks = TaskDb.getInstance().getAll();
        ArrayList<TaskAttributes> results = allTasks.stream()
                .filter(task -> {
                    for (String keyword : keywords) {
                        if (!task.getDescription().matches(String.format("(?i:.*\\b%s\\b.*)", keyword))) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toCollection(ArrayList::new));
        eventBus.post(new SearchDoneEvent(results, keywords));
        System.out.println(results);
    }
}
