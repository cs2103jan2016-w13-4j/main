// @@author A0129538W

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

        controller.switchContext(ListStatus.INCOMPLETE, true);
        controller.relayFb(String.format(Constants.CMD_SUCCESS_USED, e.getNewDirectory()), MsgType.SUCCESS);
        controller.updateNotiBubbles();

        controller.updateAutoCompleteList();
    }

    @Subscribe
    public void handleUseDirectoryFailEvent(UseDirectoryFailedEvent e) {

        switch (e.getError()) {
            case INVALID_PATH:
                controller.relayFb(String.format(Constants.CMD_ERROR_USE_FAIL_INVALID, e.getNewDirectory()),
                        MsgType.ERROR);
                break;
            default:
                break;
        }
    }
}
