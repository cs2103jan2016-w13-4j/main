// @@author A0129538W

package jfdi.ui.commandhandlers;

import java.time.format.DateTimeFormatter;

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
        editSurpriseOverlay(e.getTask());
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
                editSurpriseOverlay(new TaskAttributes());
                controller.showSurpriseDisplay();
                controller.showNoSurpriseDisplay();
                controller.relayFb(Constants.CMD_ERROR_SURP_FAIL_NO_TASKS, MsgType.ERROR);
                controller.updateNotiBubbles();
                break;
            default:
                break;
        }
    }

    private void editSurpriseOverlay(TaskAttributes task) {
        controller.taskDesc.setText(task.getDescription());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy h:mma");
        if (task.getStartDateTime() == null) {
            if (task.getEndDateTime() == null) {
                // Floating Tasks
                controller.taskTime.setText(Constants.ITEM_NO_TIMEDATE);
            } else {
                // Deadline Tasks
                String end = formatter.format(task.getEndDateTime());
                controller.taskTime.setText(String.format(Constants.ITEM_DEADLINE, end));
            }
        }
    }
}
