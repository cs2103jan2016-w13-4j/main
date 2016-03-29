package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.CommandUndoneEvent;
import jfdi.logic.events.UndoFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.ui.Constants;
import jfdi.ui.Constants.MsgType;

public class UndoHandler extends CommandHandler {

    private static UndoHandler instance = new UndoHandler();
    
    private UndoHandler() {
    }
    
    public static UndoHandler getInstance() {
        return instance;
    }
    
    @Subscribe
    public void handleCommandUndoneEvent(CommandUndoneEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        Class<? extends Command> cmdType = e.getCommandType();
        switchContext(controller.displayStatus, true);
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_UNDONE, cmdType.toString()),
            MsgType.SUCCESS);
        controller.updateNotiBubbles();

        controller.updateAutoCompleteList();
    }
    
    @Subscribe
    public void handleUndoFailedEvent(UndoFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_UNDO_FAIL_UNKNOWN,
                    MsgType.ERROR);
                break;
            case NONTHING_TO_UNDO:
                controller.relayFb(Constants.CMD_ERROR_UNDO_FAIL_NO_TASKS,
                    MsgType.ERROR);
                break;
            default:
                break;
        }
    }

}
