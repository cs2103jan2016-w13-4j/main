// @@author A0129538W

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

        controller.highLight = e.getTask();
        controller.initSurpriseOverlay(e.getTask());
        controller.switchContext(ListStatus.ALL, false);
        controller.switchContext(ListStatus.SURPRISE, false);
        controller.showSurpriseDisplay();
        controller.updateNotiBubbles();
        controller.relayFb(Constants.CMD_SUCCESS_SURPRISED, MsgType.SUCCESS);
    }

    @Subscribe
    public void handleNoSurpriseEvent(NoSurpriseEvent e) {

        switch (e.getError()) {
            case NO_TASKS:
                controller.importantList.clear();
                controller.switchContext(ListStatus.ALL, false);
                controller.switchContext(ListStatus.SURPRISE, false);
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
