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
        ArrayList<TaskAttributes> fullMatches = taskDb.getAll().stream()
                .map(this::constructFullMatchCancidate)
                .filter(this::isValidCandidate)
                .sorted(this::candidateCompare)
                .map(ImmutableTriple::getRight)
                .collect(Collectors.toCollection(ArrayList::new));

        results = new ArrayList<>(fullMatches);

        results.addAll(taskDb.getAll().stream()
                .map(this::constructStemMatchCandidate)
                .filter(this::isValidCandidate)
                .sorted(this::candidateCompare)
                .map(ImmutableTriple::getRight)
                .filter(task -> !fullMatches.contains(task))
                .collect(Collectors.toCollection(ArrayList::new)));

        logger.info(String.format("Search completed. %d results found.", results.size()));

        eventBus.post(new SearchDoneEvent(results, keywords));
    }

    private ImmutableTriple<Long, Integer, TaskAttributes> constructFullMatchCancidate(TaskAttributes task) {
        String[] parts = task.getDescription().split("\\s+");
        int wordCount = parts.length;

        long rank = Arrays.stream(parts)
            .filter(this::isFullMatch)
            .count();

        return new ImmutableTriple<Long, Integer, TaskAttributes>(rank, wordCount, task);
    }

    private ImmutableTriple<Long, Integer, TaskAttributes> constructStemMatchCandidate(TaskAttributes task) {
        String[] parts = task.getDescription().split("\\s+");
        int wordCount = parts.length;

        long rank = Arrays.stream(parts)
                .map(stemmer::stem)
                .filter(this::isStemMatch)
                .count();

        return new ImmutableTriple<Long, Integer, TaskAttributes>(rank, wordCount, task);
    }

    private boolean isValidCandidate(ImmutableTriple<Long, Integer, TaskAttributes> candidate) {
        return candidate.getLeft() > 0;
    }

    private boolean isFullMatch(String word) {
        return keywords.stream()
                .reduce(
                    false,
                    (isMatched, keyword) -> isMatched || word.equalsIgnoreCase(keyword),
                    (isPreviouslyMatched, isNowMatched) -> isPreviouslyMatched || isNowMatched
                );
    }

    private boolean isStemMatch(String word) {
        return keywords.stream()
                .map(stemmer::stem)
                .reduce(
                        false,
                        (isMatched, keyword) -> isMatched || word.equalsIgnoreCase(keyword),
                        (isPreviouslyMatched, isNowMatched) -> isPreviouslyMatched || isNowMatched
                );
    }

    private int candidateCompare(ImmutableTriple<Long, Integer, TaskAttributes> left,
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
