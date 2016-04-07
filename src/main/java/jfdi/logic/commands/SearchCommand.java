// @@author A0130195M

package jfdi.logic.commands;

import jfdi.logic.events.SearchDoneEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import opennlp.tools.stemmer.PorterStemmer;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * @author Liu Xinan
 */
public class SearchCommand extends Command {

    private static final PorterStemmer stemmer = new PorterStemmer();

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
        results = taskDb.getAll().stream()
                .map(this::constructCandidate)
                .filter(this::isValidCandidate)
                .sorted(this::candidateCompareTo)
                .map(ImmutableTriple::getRight)
                .collect(Collectors.toCollection(ArrayList::new));

        eventBus.post(new SearchDoneEvent(results, keywords));
    }

    private ImmutableTriple<Long, Integer, TaskAttributes> constructCandidate(TaskAttributes task) {
        String[] parts = task.getDescription().split("\\s+");
        int wordCount = parts.length;

        long rank = Arrays.stream(parts)
                .map(stemmer::stem)
                .filter(this::isMatched)
                .count();

        return new ImmutableTriple<Long, Integer, TaskAttributes>(rank, wordCount, task);
    }

    private boolean isValidCandidate(ImmutableTriple<Long, Integer, TaskAttributes> candidate) {
        return candidate.getLeft() > 0;
    }

    private boolean isMatched(String word) {
        return keywords.stream()
                .map(stemmer::stem)
                .reduce(
                    false,
                    (matched, keyword) -> matched || word.equalsIgnoreCase(keyword),
                    (previouslyMatched, nowMatched) -> previouslyMatched || nowMatched
                );
    }

    private int candidateCompareTo(ImmutableTriple<Long, Integer, TaskAttributes> left,
                                   ImmutableTriple<Long, Integer, TaskAttributes> right) {

        if (left.getLeft() > right.getLeft()) {
            return -1;
        } else if (left.getLeft() < right.getLeft()) {
            return 1;
        } else {
            return left.getMiddle() - right.getMiddle();
        }
    }

    @Override
    public void undo() {
        assert false;
    }

}
