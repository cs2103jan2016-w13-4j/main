// @@author A0129538W

package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.ExitCalledEvent;
import jfdi.ui.Constants;

public class ExitHandler extends CommandHandler {

    private static ExitHandler instance = new ExitHandler();

    private ExitHandler() {
    }

    public static ExitHandler getInstance() {
        return instance;
    }

    @Subscribe
    public void handleExitCalledEvent(ExitCalledEvent e) {

        System.out.printf("\nMoriturus te saluto.\n");
        System.exit(0);
        logger.fine(Constants.LOG_USER_EXIT);
    }
}
