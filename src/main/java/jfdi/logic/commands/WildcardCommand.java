package jfdi.logic.commands;

import jfdi.logic.events.NoSurpriseEvent;
import jfdi.logic.events.SurpriseEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.apis.TaskDb;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * @author Xinan
 */
public class WildcardCommand extends Command {

    private WildcardCommand(Builder builder) {}

    public static class Builder {

        public WildcardCommand build() {
            return new WildcardCommand(this);
        }

    }

    @Override
    public void execute() {
        ArrayList<TaskAttributes> allTasks = new ArrayList<>(TaskDb.getInstance().getAll());

        SecureRandom random = new SecureRandom();

        if (!allTasks.isEmpty()) {
            TaskAttributes lucky = allTasks.get(random.nextInt(allTasks.size()));
            eventBus.post(new SurpriseEvent(lucky));
        } else {
            eventBus.post(new NoSurpriseEvent(NoSurpriseEvent.Error.NO_TASKS));
        }
    }

    @Override
    public void undo() {
        throw new UnsupportedOperationException();
    }
}
