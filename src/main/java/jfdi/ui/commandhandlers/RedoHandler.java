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

        Class<? extends Command> cmdType = e.getCommandType();
        controller.switchContext(controller.displayStatus, true);

        controller.updateAutoCompleteList();

        controller.updateNotiBubbles();
        controller.relayFb(String.format(Constants.CMD_SUCCESS_REDONE, cmdType.toString()), MsgType.SUCCESS);
    }

    @Subscribe
    public void handleRedoFailedEvent(RedoFailedEvent e) {

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
