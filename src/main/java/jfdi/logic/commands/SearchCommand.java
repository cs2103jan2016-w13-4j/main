// @@author A0130195M

package jfdi.logic.commands;

import jfdi.logic.events.SearchDoneEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * @author Liu Xinan
 */
public class SearchCommand extends Command {

    private HashSet<String> keywords;

    private ArrayList<TaskAttributes> results;

    private SearchCommand(Builder builder) {
        this.keywords = builder.keywords;
    }

    public HashSet<String> getKeywords() {
        return keywords;
    }

    public ArrayList<TaskAttributes> getResults() {
        return results;
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
        results = new ArrayList<>();
        results.addAll(getFullMatches());
        results.addAll(getPartialMatches());

        eventBus.post(new SearchDoneEvent(results, keywords));
    }

    private ArrayList<TaskAttributes> getFullMatches() {
        return taskDb.getAll().stream().filter(task -> {
            for (String keyword : keywords) {
                if (!task.getDescription().matches(String.format("(?i:.*\\b%s\\b.*)", keyword))) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<TaskAttributes> getPartialMatches() {
        return taskDb.getAll().stream().filter(task -> {
            int matches = 0;
            for (String keyword : keywords) {
                if (task.getDescription().matches(String.format("(?i:.*\\b%s\\b.*)", keyword))) {
                    matches++;
                }
            }
            return matches != 0 && matches != keywords.size();
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void undo() {
        assert false;
    }

}
