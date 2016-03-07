package jfdi.logic.commands;

import jfdi.CoverageIgnore;
import jfdi.logic.interfaces.Command;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * @author Liu Xinan
 */
public class InvalidCommand extends Command {

    private static ArrayList<Consumer<InvalidCommand>> successHooks = new ArrayList<>();
    private static ArrayList<Consumer<InvalidCommand>> failureHooks = new ArrayList<>();

    private InvalidCommand(Builder builder) {}

    public static class Builder {

        public InvalidCommand build() {
            return new InvalidCommand(this);
        }

    }

    @Override
    public void execute() {
        // Invalid command always fail.
        onFailure();
    }

    @Override
    @CoverageIgnore
    protected void onSuccess() {
        for (Consumer<InvalidCommand> hook : successHooks) { // $COVERAGE-IGNORE$
            hook.accept(this);
        }
    }

    @Override
    protected void onFailure() {
        for (Consumer<InvalidCommand> hook : failureHooks) {
            hook.accept(this);
        }
    }

    /**
     * Add a hook to be run when the command executes successfully.
     * The hook will be passed the command object as argument.
     *
     * @param hook Command hook to be run upon success
     */
    public static void addSuccessHook(Consumer<InvalidCommand> hook) {
        successHooks.add(hook);
    }

    /**
     * Add a hook to be run when the command encounters error.
     * The hook will be passed the command object as argument.
     *
     * @param hook Command hook to be run upon failure
     */
    public static void addFailureHook(Consumer<InvalidCommand> hook) {
        failureHooks.add(hook);
    }

}
