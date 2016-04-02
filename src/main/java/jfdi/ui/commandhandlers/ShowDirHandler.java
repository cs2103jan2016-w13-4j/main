package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.ShowDirectoryEvent;
import jfdi.ui.Constants;
import jfdi.ui.Constants.MsgType;

public class ShowDirHandler extends CommandHandler {

    private static ShowDirHandler instance = new ShowDirHandler();

    private ShowDirHandler() {
    }

    public static ShowDirHandler getInstance() {
        return instance;
    }

    @Subscribe
    public void handleShowDirectoryEvent(ShowDirectoryEvent e) {

        controller.updateNotiBubbles();
        controller.relayFb(String.format(Constants.CMD_SUCCESS_SHOWDIRECTORY, e.getPwd()), MsgType.SUCCESS);
    }

}
