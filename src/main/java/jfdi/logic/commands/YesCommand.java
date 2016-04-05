package jfdi.logic.commands;

import jfdi.logic.ControlCenter;
import jfdi.logic.interfaces.Command;

/**
 * @author Liu Xinan
 */
public class YesCommand extends Command {

    @Override
    public void execute() {
        ControlCenter cc = ControlCenter.getInstance();
        getLastSuggestion().ifPresent(cc::handleInput);
    }

    @Override
    public void undo() {
        assert false;
    }
}
