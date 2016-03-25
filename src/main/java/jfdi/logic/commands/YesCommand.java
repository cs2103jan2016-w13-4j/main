package jfdi.logic.commands;

import jfdi.logic.ControlCenter;
import jfdi.logic.interfaces.Command;

/**
 * @author Xinan
 */
public class YesCommand extends Command {

    @Override
    public void execute() {
        ControlCenter cc = ControlCenter.getInstance();
        getLastSuggestion().ifPresent(suggestion -> cc.handleInput(suggestion));
    }

    @Override
    public void undo() {
        assert false;
    }
}
