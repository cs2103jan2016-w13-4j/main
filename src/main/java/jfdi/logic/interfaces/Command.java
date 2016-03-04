package jfdi.logic.interfaces;

/**
 * @author Liu Xinan
 */
public abstract class Command {

    /**
     * Executes the command.
     */
    public abstract void execute();

    /**
     * To be implemented by each command to run the success command hooks.
     * Pass a reference of self to each hook.
     */
    protected abstract void onSuccess();

    /**
     * To be implemented by each command to run the failure command hooks.
     * Pass a reference of self to each hook.
     */
    protected abstract void onFailure();

}
