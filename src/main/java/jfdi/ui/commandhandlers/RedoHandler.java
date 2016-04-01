package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.CommandRedoneEvent;
import jfdi.logic.events.RedoFailedEvent;
import jfdi.logic.interfaces.Command;
import jfdi.ui.Constants;
import jfdi.ui.Constants.MsgType;

public class RedoHandler extends CommandHandler {

    private static RedoHandler instance = new RedoHandler();

    private RedoHandler() {
    }

    public static RedoHandler getInstance() {
        return instance;
    }

    @Subscribe
    public void handleCommandRedoneEvent(CommandRedoneEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        Class<? extends Command> cmdType = e.getCommandType();
        switchContext(controller.displayStatus, true);
        controller.relayFb(String.format(Constants.CMD_SUCCESS_REDONE, cmdType.toString()), MsgType.SUCCESS);
        controller.updateNotiBubbles();

        controller.updateAutoCompleteList();
    }

    @Subscribe
    public void handleRedoFailedEvent(RedoFailedEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_REDO_FAIL_UNKNOWN, MsgType.ERROR);
                break;
            case NONTHING_TO_REDO:
                controller.relayFb(Constants.CMD_ERROR_REDO_FAIL_NO_TASKS, MsgType.ERROR);
                break;
            default:
                break;
        }
    }
}
