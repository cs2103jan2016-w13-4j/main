package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.HelpRequestedEvent;
import jfdi.ui.Constants;
import jfdi.ui.Constants.ListStatus;
import jfdi.ui.Constants.MsgType;

public class HelpHandler extends CommandHandler {

    private static HelpHandler instance = new HelpHandler();
    
    private HelpHandler() {
    }
    
    public static HelpHandler getInstance() {
        return instance;
    }
    
    @Subscribe
    public void handleHelpRequestEvent(HelpRequestedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        switchContext(ListStatus.HELP, false);
        controller.showHelpDisplay();
        controller.relayFb(Constants.CMD_SUCCESS_HELP, MsgType.SUCCESS);
    }
}
