package jfdi.logic.commands;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

import jfdi.logic.interfaces.Command;
import jfdi.storage.data.TaskAttributes;
import jfdi.storage.data.TaskDb;

/**
 * @author Liu Xinan
 */
public class AddCommandStub extends Command {

    public enum ErrorType {
        NON_EXISTENT_TAG, UNKNOWN
    }

    private static ArrayList<Consumer<AddCommandStub>> successHooks = new ArrayList<>();
    private static ArrayList<Consumer<AddCommandStub>> failureHooks = new ArrayList<>();

    private ArrayList<String> tags;
    private String description;
    private ArrayList<LocalDateTime> dateTimes;
    private Collection<TaskAttributes> items = null;
    private ErrorType errorType = null;

    private AddCommandStub(Builder builder) {
        this.tags = builder.tags;
        this.setDateTimes(builder.dateTimes);
        this.setDescription(builder.description);
    }

    public static class Builder {

        ArrayList<String> tags = new ArrayList<>();
        String description = null;
        ArrayList<LocalDateTime> dateTimes = new ArrayList<>();

        public Builder addTag(String tag) {
            this.tags.add(tag);
            return this;
        }

        public Builder addTags(ArrayList<String> tags) {
            this.tags.addAll(tags);
            return this;
        }

        public Builder addDateTimes(ArrayList<LocalDateTime> dateTimes) {
            this.dateTimes.addAll(dateTimes);
            return this;
        }

        public Builder addDescription(String s) {
            this.description = s;
            return this;
        }

        public AddCommandStub build() {
            return new AddCommandStub(this);
        }
    }

    /**
     * Get the items to for listing.
     *
     * @return A Collection of Tasks resulted from the command.
     */
    public Collection<TaskAttributes> getItems() {
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

    @Override
    public void execute() {
        if (tags.isEmpty()) {
            items = TaskDb.getAll();
            onSuccess();
        } else {
            // TODO: Add filtering when Task supports that.
            errorType = ErrorType.UNKNOWN;
            onFailure();
        }
    }

    protected void onSuccess() {
        for (Consumer<AddCommandStub> hook : successHooks) {
            hook.accept(this);
        }
    }

    protected void onFailure() {
        for (Consumer<AddCommandStub> hook : failureHooks) {
            hook.accept(this);
        }
    }

    /**
     * Add a hook to be run when the command executes successfully. The hook
     * will be passed the command object as argument.
     *
     * @param hook
     *            Command hook to be run upon success
     */
    public static void addSuccessHook(Consumer<AddCommandStub> hook) {
        successHooks.add(hook);
    }

    /**
     * Add a hook to be run when the command encounters error. The hook will be
     * passed the command object as argument.
     *
     * @param hook
     *            Command hook to be run upon failure
     */
    public static void addFailureHook(Consumer<AddCommandStub> hook) {
        failureHooks.add(hook);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<LocalDateTime> getDateTimes() {
        return dateTimes;
    }

    public void setDateTimes(ArrayList<LocalDateTime> dateTimes) {
        this.dateTimes = dateTimes;
    }
}
