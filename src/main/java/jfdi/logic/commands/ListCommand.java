package jfdi.logic.commands;

import jfdi.logic.interfaces.Command;
import jfdi.storage.records.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * @author Liu Xinan
 */
public class ListCommand extends Command {

    public enum ErrorType {
        NON_EXISTENT_TAG, UNKNOWN
    }

    private static ArrayList<Consumer<ListCommand>> successHooks = new ArrayList<>();
    private static ArrayList<Consumer<ListCommand>> failureHooks = new ArrayList<>();

    private ArrayList<String> tags;
    private ArrayList<Task> items = null;
    private ErrorType errorType = null;

    private ListCommand(Builder builder) {
        this.tags = builder.tags;
    }

    public static class Builder {

        ArrayList<String> tags = new ArrayList<>();

        public Builder addTag(String tag) {
            this.tags.add(tag);
            return this;
        }

        public Builder addTags(ArrayList<String> tags) {
            this.tags.addAll(tags);
            return this;
        }

        public ListCommand build() {
            return new ListCommand(this);
        }
    }

    /**
     * Get the items to for listing.
     *
     * @return A Collection of Tasks resulted from the command.
     */
    public Collection<Task> getItems() {
        if (items == null) {
            throw new IllegalStateException("Command not yet executed!");
        }

        return items;
    }

    /**
     * Get the error type that the command encountered.
     *
     * @return Error type
     */
    public ErrorType getErrorType() {
        return errorType;
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
        items = new ArrayList<>();
        if (tags.isEmpty()) {
            items.addAll(Task.getAll());
        } else {
            for (String tag : tags) {
                items.addAll(Task.getByTag(tag));
            }
        }

        if (items.isEmpty()) {
            errorType = ErrorType.NON_EXISTENT_TAG;
            onFailure();
        } else {
            onSuccess();
        }
    }

    @Override
    protected void onSuccess() {
        for (Consumer<ListCommand> hook : successHooks) {
            hook.accept(this);
        }
    }

    @Override
    protected void onFailure() {
        for (Consumer<ListCommand> hook : failureHooks) {
            hook.accept(this);
        }
    }

    /**
     * Add a hook to be run when the command executes successfully.
     * The hook will be passed the command object as argument.
     *
     * @param hook Command hook to be run upon success
     */
    public static void addSuccessHook(Consumer<ListCommand> hook) {
        successHooks.add(hook);
    }

    /**
     * Add a hook to be run when the command encounters error.
     * The hook will be passed the command object as argument.
     *
     * @param hook Command hook to be run upon failure
     */
    public static void addFailureHook(Consumer<ListCommand> hook) {
        failureHooks.add(hook);
    }
}
