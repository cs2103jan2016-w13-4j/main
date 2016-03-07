package jfdi.logic.interfaces;

import com.google.common.eventbus.EventBus;

import jfdi.ui.UserInterface;

/**
 * @author Liu Xinan
 */
public abstract class Command {

    protected EventBus eventBus = UserInterface.getEventBus();

    /**
     * Executes the command.
     */
    public abstract void execute();

}
