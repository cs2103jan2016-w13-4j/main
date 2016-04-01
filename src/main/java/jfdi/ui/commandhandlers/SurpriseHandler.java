package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.NoSurpriseEvent;
import jfdi.logic.events.SurpriseEvent;
import jfdi.storage.apis.TaskAttributes;
import jfdi.ui.Constants;
import jfdi.ui.Constants.ListStatus;
import jfdi.ui.Constants.MsgType;

public class SurpriseHandler extends CommandHandler {

    private static SurpriseHandler instance = new SurpriseHandler();

    private SurpriseHandler() {
    }

    public static SurpriseHandler getInstance() {
        return instance;
    }

    @Subscribe
    public void handleSurpriseEvent(SurpriseEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        controller.importantList.clear();
        controller.initSurpriseOverlay(e.getTask());
        switchContext(ListStatus.ALL, false);
        switchContext(ListStatus.SURPRISE, false);
        controller.relayFb(Constants.CMD_SUCCESS_SURPRISED, MsgType.SUCCESS);
        controller.updateNotiBubbles();
        controller.showSurpriseDisplay();
    }

    @Subscribe
    public void handleNoSurpriseEvent(NoSurpriseEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_SURP_FAIL_UNKNOWN, MsgType.ERROR);
                break;
            case NO_TASKS:
                controller.importantList.clear();
                switchContext(ListStatus.ALL, false);
                switchContext(ListStatus.SURPRISE, false);
                controller.initSurpriseOverlay(new TaskAttributes());
                controller.showSurpriseDisplay();
                controller.showNoSurpriseDisplay();
                controller.relayFb(Constants.CMD_ERROR_SURP_FAIL_NO_TASKS, MsgType.ERROR);
                controller.updateNotiBubbles();
                break;
            default:
                break;
        }
    }
}
