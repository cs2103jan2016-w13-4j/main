package jfdi.logic.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.apis.TaskDb;

/**
 * @author Liu Xinan
 */
public class DeleteCommandStub extends Command {

    public enum ErrorType {
        NON_EXISTENT_TAG, UNKNOWN
    }

    private static ArrayList<Consumer<DeleteCommandStub>> successHooks = new ArrayList<>();
    private static ArrayList<Consumer<DeleteCommandStub>> failureHooks = new ArrayList<>();

    private ArrayList<String> taskIds;
    private Collection<TaskAttributes> items = null;
    private ErrorType errorType = null;

    private DeleteCommandStub(Builder builder) {
        this.taskIds = builder.taskIds;
    }

    public static class Builder {

        ArrayList<String> taskIds = null;

        public Builder addTaskIds(ArrayList<String> taskIds) {
            this.taskIds = taskIds;
            return this;
        }

        public DeleteCommandStub build() {
            return new DeleteCommandStub(this);
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
        if (taskIds.isEmpty()) {
            items = TaskDb.getInstance().getAll();
            onSuccess();
        } else {
            // TODO: Add filtering when Task supports that.
            errorType = ErrorType.UNKNOWN;
            onFailure();
        }
    }

    protected void onSuccess() {
        for (Consumer<DeleteCommandStub> hook : successHooks) {
            hook.accept(this);
        }
    }

    protected void onFailure() {
        for (Consumer<DeleteCommandStub> hook : failureHooks) {
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
    public static void addSuccessHook(Consumer<DeleteCommandStub> hook) {
        successHooks.add(hook);
    }

    /**
     * Add a hook to be run when the command encounters error. The hook will be
     * passed the command object as argument.
     *
     * @param hook
     *            Command hook to be run upon failure
     */
    public static void addFailureHook(Consumer<DeleteCommandStub> hook) {
        failureHooks.add(hook);
    }
}
