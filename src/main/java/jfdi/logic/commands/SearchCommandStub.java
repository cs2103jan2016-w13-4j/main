package jfdi.logic.commands;

import jfdi.logic.interfaces.Command;

import java.util.Collection;
import java.util.HashSet;

public class SearchCommandStub extends Command {

    Collection<String> keywords = new HashSet<>();

    public SearchCommandStub(Builder builder) {
        this.keywords = builder.keywords;
    }

    public static class Builder {

        Collection<String> keywords = new HashSet<>();

        public Builder setKeywords(Collection<String> keywords) {
            this.keywords.addAll(keywords);
            return this;
        }

        public SearchCommandStub build() {
            return new SearchCommandStub(this);
        }
    }

    @Override
    public void execute() {

    }
}
