package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.ExitCalledEvent;
import jfdi.ui.Constants;

public class ExitHandler extends CommandHandler {

    
    @Subscribe
    public void handleExitCalledEvent(ExitCalledEvent e) {
        if (controller.isInternalCall()) {
            // Add any method calls strictly for internal calls here
            return;
        }

        System.out.printf("\nMoriturus te saluto.\n");
        System.exit(0);
        logger.fine(Constants.LOG_USER_EXIT);
    }
}
