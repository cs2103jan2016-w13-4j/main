package jfdi.logic.interfaces;

import com.google.common.eventbus.EventBus;

import jfdi.ui.UI;

/**
 * @author Liu Xinan
 */
public abstract class Command {

    protected EventBus eventBus = UI.getEventBus();

    /**
     * Executes the command.
     */
    public abstract void execute();

}
