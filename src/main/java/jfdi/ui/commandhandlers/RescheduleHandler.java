package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.RescheduleTaskDoneEvent;
import jfdi.logic.events.RescheduleTaskFailedEvent;
import jfdi.storage.apis.TaskAttributes;
import jfdi.ui.Constants;
import jfdi.ui.Constants.MsgType;

public class RescheduleHandler extends CommandHandler {

    private static RescheduleHandler instance = new RescheduleHandler();

    private RescheduleHandler() {
    }

    public static RescheduleHandler getInstance() {
        return instance;
    }

    @Subscribe
    public void handleRescheduleTaskDoneEvent(RescheduleTaskDoneEvent e) {

        TaskAttributes task = e.getTask();

        int countBef = findCurrentIndex(task);

        controller.transListCmd();

        if (controller.shouldSort()) {
            controller.sortDisplayList();
        }

        int countAft = findCurrentIndex(task);

        logger.fine(String.format(Constants.LOG_RESCHED_SUCCESS, task.getId()));
        controller.listMain.scrollTo(countAft);
        controller.updateNotiBubbles();
        controller.relayFb(String.format(Constants.CMD_SUCCESS_RESCHEDULED, countBef, countAft), MsgType.SUCCESS);
    }

    @Subscribe
    public void handleRescheduleTaskFailEvent(RescheduleTaskFailedEvent e) {

        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_CANT_RESCHEDULE_UNKNOWN, MsgType.ERROR);
                logger.fine(Constants.LOG_RESCHE_FAIL_UNKNOWN);
                break;
            case NON_EXISTENT_ID:
                // NEED TO CHANGE TO INDEX SOON????
                controller.relayFb(String.format(Constants.CMD_ERROR_CANT_RESCHEDULE_NO_ID, e.getScreenId()),
                        MsgType.ERROR);
                logger.fine(Constants.LOG_RESCHE_FAIL_NOID);
                break;
            case NO_CHANGES:
                controller.relayFb(Constants.CMD_ERROR_CANT_RESCHEDULE_NO_CHANGES + e.getStartDateTime() + " - to - "
                        + e.getEndDateTime() + " -!", MsgType.ERROR);
                logger.fine(Constants.LOG_RESCHE_FAIL_NOCHANGE);
                break;
            case DUPLICATED_TASK:
                controller.relayFb(Constants.CMD_ERROR_CANT_RESCHEDULE_DUPLICATE, MsgType.ERROR);
                logger.fine(String.format(Constants.LOG_RESCHE_FAIL_DUPLICATE));
                break;
            default:
                break;
        }
    }

}
