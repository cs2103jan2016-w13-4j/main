// @@author A0129538W

package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.InvalidCommandEvent;
import jfdi.ui.Constants;
import jfdi.ui.Constants.MsgType;

public class InvalidCmdHandler extends CommandHandler {

    private static InvalidCmdHandler instance = new InvalidCmdHandler();

    private InvalidCmdHandler() {
    }

    public static InvalidCmdHandler getInstance() {
        return instance;
    }

    @Subscribe
    public void handleInvalidCommandEvent(InvalidCommandEvent e) {

        controller.relayFb(String.format(Constants.CMD_WARNING_DONTKNOW, e.getInputString()), MsgType.WARNING);
        logger.fine(Constants.LOG_INVALID_COMMAND);
    }

}
