package jfdi.ui.commandhandlers;

import java.util.logging.Logger;

import jfdi.common.utilities.JfdiLogger;
import jfdi.storage.apis.TaskAttributes;
import jfdi.ui.MainController;
import jfdi.ui.UI;

public abstract class CommandHandler {

    public static Logger logger = JfdiLogger.getLogger();
    public MainController controller = UI.getInstance().controller;

    public void setController(MainController ctrl) {
        controller = ctrl;
    }

    public static void registerEvents() {
        UI.getEventBus().register(AddHandler.getInstance());
        UI.getEventBus().register(AliasHandler.getInstance());
        UI.getEventBus().register(DeleteHandler.getInstance());
        UI.getEventBus().register(ExitHandler.getInstance());
        UI.getEventBus().register(HelpHandler.getInstance());
        UI.getEventBus().register(InitializationHandler.getInstance());
        UI.getEventBus().register(InvalidCmdHandler.getInstance());
        UI.getEventBus().register(ListHandler.getInstance());
        UI.getEventBus().register(MarkHandler.getInstance());
        UI.getEventBus().register(MoveDirHandler.getInstance());
        UI.getEventBus().register(RedoHandler.getInstance());
        UI.getEventBus().register(RenameHandler.getInstance());
        UI.getEventBus().register(RescheduleHandler.getInstance());
        UI.getEventBus().register(SearchHandler.getInstance());
        UI.getEventBus().register(ShowDirHandler.getInstance());
        UI.getEventBus().register(SurpriseHandler.getInstance());
        UI.getEventBus().register(UnaliasHandler.getInstance());
        UI.getEventBus().register(UndoHandler.getInstance());
        UI.getEventBus().register(UnmarkHandler.getInstance());
        UI.getEventBus().register(UseDirHandler.getInstance());
    }

    public int findCurrentIndex(TaskAttributes task) {

        int count = 0;

        for (int i = 0; i < controller.importantList.size(); i++) {
            if (controller.getIdFromIndex(i) == task.getId()) {
                count = i;
                break;
            }
        }
        return count + 1;
    }
}
