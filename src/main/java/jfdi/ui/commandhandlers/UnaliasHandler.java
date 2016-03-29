package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.UnaliasDoneEvent;
import jfdi.logic.events.UnaliasFailedEvent;
import jfdi.ui.Constants;
import jfdi.ui.Constants.MsgType;

public class UnaliasHandler extends CommandHandler {
    
    @Subscribe
    public void handleUnaliasDoneEvent(UnaliasDoneEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        controller.relayFb(
            String.format(Constants.CMD_SUCCESS_UNALIAS, e.getAlias()),
            MsgType.SUCCESS);

        controller.updateAutoCompleteList();
    }

    @Subscribe
    public void handleUnaliasFailEvent(UnaliasFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(
                    String.format(Constants.CMD_ERROR_CANT_UNALIAS_UNKNOWN,
                        e.getAlias()), MsgType.ERROR);
                // logger.fine(Constants.LOG_RESCHE_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ALIAS:
                // NEED TO CHANGE TO INDEX SOON????
                controller.relayFb(
                    String.format(Constants.CMD_ERROR_CANT_UNALIAS_NO_ALIAS,
                        e.getAlias()), MsgType.ERROR);
                // logger.fine(Constants.LOG_RESCHE_FAIL_NOID);
                break;
            default:
                break;
        }
    }
}
