package jfdi.logic.commands;

import jfdi.CoverageIgnore;
import jfdi.logic.interfaces.AbstractCommand;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * @author Liu Xinan
 */
public class ExitCommand extends AbstractCommand {

    private static ArrayList<Consumer<ExitCommand>> successHooks = new ArrayList<>();
    private static ArrayList<Consumer<ExitCommand>> failureHooks = new ArrayList<>();

    private ExitCommand(Builder builder) {}

    public static class Builder {

        public ExitCommand build() {
            return new ExitCommand(this);
        }

    }

    @Override
    public void execute() {
        // Nothing needs to be done.
        // Call success hooks and UI will exit.
        onSuccess();
    }

    @Override
    protected void onSuccess() {
        for (Consumer<ExitCommand> hook : successHooks) {
            hook.accept(this);
        }
    }

    @Override
    @CoverageIgnore
    protected void onFailure() {
        for (Consumer<ExitCommand> hook : failureHooks) {
            hook.accept(this);
        }
    }

    /**
     * Add a hook to be run when the command executes successfully.
     * The hook will be passed the command object as argument.
     *
     * @param hook Command hook to be run upon success
     */
    public static void addSuccessHook(Consumer<ExitCommand> hook) {
        successHooks.add(hook);
    }

    /**
     * Add a hook to be run when the command encounters error.
     * The hook will be passed the command object as argument.
     *
     * @param hook Command hook to be run upon failure
     */
    public static void addFailureHook(Consumer<ExitCommand> hook) {
        failureHooks.add(hook);
    }
}
