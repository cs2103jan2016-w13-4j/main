package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.ShowDirectoryEvent;
import jfdi.ui.Constants;
import jfdi.ui.Constants.MsgType;

public class ShowDirHandler extends CommandHandler {

    @Subscribe
    public void handleShowDirectoryEvent(ShowDirectoryEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_SHOWDIRECTORY, e.getPwd()),
            MsgType.SUCCESS);
        controller.updateNotiBubbles();
    }

}
