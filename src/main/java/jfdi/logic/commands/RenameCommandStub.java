package jfdi.logic.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

import jfdi.logic.interfaces.AbstractCommand;
import jfdi.storage.records.Task;

/**
 * @author Liu Xinan
 */
public class RenameCommandStub extends AbstractCommand {

    public enum ErrorType {
        NON_EXISTENT_TAG, UNKNOWN
    }

    private static ArrayList<Consumer<RenameCommandStub>> successHooks = new ArrayList<>();
    private static ArrayList<Consumer<RenameCommandStub>> failureHooks = new ArrayList<>();

    private String taskId;
    private String taskDescription;
    private Collection items = null;
    private ErrorType errorType = null;

    private RenameCommandStub(Builder builder) {
        this.taskId = builder.taskId;
        this.taskDescription = builder.taskDescription;
    }

    public static class Builder {

        String taskId = null;
        String taskDescription = null;

        public Builder addTaskId(String taskId) {
            this.taskId = taskId;
            return this;
        }

        public Builder addTaskDescription(String taskDescription) {
            this.taskDescription = taskDescription;
            return this;
        }

        public RenameCommandStub build() {
            return new RenameCommandStub(this);
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

    @Override
    public void execute() {
        if (taskId.isEmpty()) {
            items = Task.getAll();
            onSuccess();
        } else {
            // TODO: Add filtering when Task supports that.
            errorType = ErrorType.UNKNOWN;
            onFailure();
        }
    }

    @Override
    protected void onSuccess() {
        for (Consumer<RenameCommandStub> hook : successHooks) {
            hook.accept(this);
        }
    }

    @Override
    protected void onFailure() {
        for (Consumer<RenameCommandStub> hook : failureHooks) {
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
    public static void addSuccessHook(Consumer<RenameCommandStub> hook) {
        successHooks.add(hook);
    }

    /**
     * Add a hook to be run when the command encounters error. The hook will be
     * passed the command object as argument.
     *
     * @param hook
     *            Command hook to be run upon failure
     */
    public static void addFailureHook(Consumer<RenameCommandStub> hook) {
        failureHooks.add(hook);
    }
}
