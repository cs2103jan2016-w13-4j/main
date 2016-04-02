package jfdi.ui.commandhandlers;

import com.google.common.eventbus.Subscribe;

import jfdi.logic.events.FilesReplacedEvent;
import jfdi.logic.events.InitializationFailedEvent;
import jfdi.storage.exceptions.FilePathPair;
import jfdi.ui.Constants;
import jfdi.ui.Constants.MsgType;

public class InitializationHandler extends CommandHandler {

    private static InitializationHandler instance = new InitializationHandler();

    private InitializationHandler() {
    }

    public static InitializationHandler getInstance() {
        return instance;
    }

    @Subscribe
    public void handleFilesReplacedEvent(FilesReplacedEvent e) {
        String fb = "";
        for (FilePathPair item : e.getFilePathPairs()) {
            fb += String.format("\n" + Constants.CMD_ERROR_INIT_FAIL_REPLACED, item.getOldFilePath(),
                    item.getNewFilePath());
        }
        controller.appendFb(fb, MsgType.WARNING);
        controller.updateNotiBubbles();
    }

    @Subscribe
    public void handleInitializationFailedEvent(InitializationFailedEvent e) {

        switch (e.getError()) {
            case UNKNOWN:
                controller.relayFb(Constants.CMD_ERROR_INIT_FAIL_UNKNOWN, MsgType.ERROR);
                break;
            case INVALID_PATH:
                controller.relayFb(String.format(Constants.CMD_ERROR_INIT_FAIL_INVALID, e.getPath()), MsgType.ERROR);
                break;
            default:
                break;
        }
    }

}
