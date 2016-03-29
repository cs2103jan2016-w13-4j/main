package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.AliasDoneEvent;
import jfdi.logic.events.AliasFailedEvent;
import jfdi.ui.Constants;
import jfdi.ui.Constants.MsgType;

public class AliasHandler extends CommandHandler {

    private static AliasHandler instance = new AliasHandler();

    private AliasHandler() {
    }

    public static AliasHandler getInstance() {
        return instance;
    }

    @Subscribe
    public void handleAliasDoneEvent(AliasDoneEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here

            return;
        }

        controller.relayFb(String.format(Constants.CMD_SUCCESS_ALIAS, e.getAlias(), e.getCommand()), MsgType.SUCCESS);

        controller.updateAutoCompleteList();
    }

    @Subscribe
    public void handleAliasFailEvent(AliasFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        switch (e.getError()) {
            case INVALID_PARAMETERS:
                controller.relayFb(String.format(Constants.CMD_ERROR_CANT_ALIAS_INVALID, e.getAlias(), e.getCommand()),
                        MsgType.ERROR);
                // logger.fine(String.format(format, args));
                break;
            case DUPLICATED_ALIAS:
                controller.relayFb(String.format(Constants.CMD_ERROR_CANT_ALIAS_DUPLICATED, e.getAlias()),
                        MsgType.ERROR);
                // logger.fine(String.format(format, args));
                break;
            case UNKNOWN:
                controller.relayFb(String.format(Constants.CMD_ERROR_CANT_ALIAS_UNKNOWN, e.getCommand()),
                        MsgType.ERROR);
                // logger.fine(String.format(format, args));
                break;
            default:
                break;
        }
    }
}
