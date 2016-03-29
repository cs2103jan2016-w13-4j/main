package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.MoveDirectoryDoneEvent;
import jfdi.logic.events.MoveDirectoryFailedEvent;
import jfdi.ui.Constants;
import jfdi.ui.Constants.ListStatus;
import jfdi.ui.Constants.MsgType;

public class MoveDirHandler extends CommandHandler {
    
    private static MoveDirHandler instance = new MoveDirHandler();
    
    private MoveDirHandler() {
    }
    
    public static MoveDirHandler getInstance() {
        return instance;
    }
    
    @Subscribe
    public void handleMoveDirectoryDoneEvent(MoveDirectoryDoneEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        switchContext(ListStatus.INCOMPLETE, true);
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_MOVED, e.getNewDirectory()),
            MsgType.SUCCESS);
        controller.updateNotiBubbles();

        controller.updateAutoCompleteList();
    }

    @Subscribe
    public void handleMoveDirectoryFailEvent(MoveDirectoryFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(
                    String.format(Constants.CMD_ERROR_MOVE_FAIL_UNKNOWN,
                        e.getNewDirectory()), MsgType.ERROR);
                break;
            case INVALID_PATH:
                controller.relayFb(
                    String.format(Constants.CMD_ERROR_MOVE_FAIL_INVALID,
                        e.getNewDirectory()), MsgType.ERROR);
                break;
            default:
                break;
        }
    }
}
