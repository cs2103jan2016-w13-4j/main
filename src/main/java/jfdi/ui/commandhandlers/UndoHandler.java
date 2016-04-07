// @@author A0129538W

package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.CommandUndoneEvent;
import jfdi.logic.events.UndoFailedEvent;
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

        controller.updateNotiBubbles();
        controller.switchContext(controller.displayStatus, true);
        controller.updateAutoCompleteList();
        controller.relayFb(Constants.CMD_SUCCESS_UNDONE, MsgType.SUCCESS);
    }

    @Subscribe
    public void handleUndoFailedEvent(UndoFailedEvent e) {

        switch (e.getError()) {
            case NONTHING_TO_UNDO:
                controller.relayFb(Constants.CMD_ERROR_UNDO_FAIL_NO_TASKS, MsgType.ERROR);
                break;
            default:
                break;
        }
    }

}
