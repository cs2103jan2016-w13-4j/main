package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.UseDirectoryDoneEvent;
import jfdi.logic.events.UseDirectoryFailedEvent;
import jfdi.ui.Constants;
import jfdi.ui.Constants.ListStatus;
import jfdi.ui.Constants.MsgType;

public class UseDirHandler extends CommandHandler {

    private static UseDirHandler instance = new UseDirHandler();

    private UseDirHandler() {
    }

    public static UseDirHandler getInstance() {
        return instance;
    }

    @Subscribe
    public void handleUseDirectoryDoneEvent(UseDirectoryDoneEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        switchContext(ListStatus.INCOMPLETE, true);
        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_USED, e.getNewDirectory()),
            MsgType.SUCCESS);
        controller.updateNotiBubbles();

        controller.updateAutoCompleteList();
    }

    @Subscribe
    public void handleUseDirectoryFailEvent(UseDirectoryFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(
                    String.format(Constants.CMD_ERROR_USE_FAIL_UNKNOWN,
                        e.getNewDirectory()), MsgType.ERROR);
                break;
            case INVALID_PATH:
                controller.relayFb(
                    String.format(Constants.CMD_ERROR_USE_FAIL_INVALID,
                        e.getNewDirectory()), MsgType.ERROR);
                break;
            default:
                break;
        }
    }
}
