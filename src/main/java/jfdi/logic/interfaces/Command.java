package jfdi.logic.interfaces;

import com.google.common.eventbus.EventBus;
import jfdi.logic.ControlCenter;

/**
 * @author Liu Xinan
 */
public abstract class Command {

    protected EventBus eventBus = ControlCenter.getEventBus();

    /**
     * Executes the command.
     */
    public abstract void execute();

}
