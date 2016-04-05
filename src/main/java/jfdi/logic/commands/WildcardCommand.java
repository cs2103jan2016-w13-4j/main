package jfdi.logic.commands;

import jfdi.logic.events.NoSurpriseEvent;
import jfdi.logic.events.SurpriseEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @@author Liu Xinan
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
        ArrayList<TaskAttributes> incompleteTasks = taskDb.getAll().stream()
                .filter(task -> !task.isCompleted() && task.getStartDateTime() == null)
                .collect(Collectors.toCollection(ArrayList::new));

        SecureRandom random = new SecureRandom();

        if (!incompleteTasks.isEmpty()) {
            TaskAttributes lucky = incompleteTasks.get(random.nextInt(incompleteTasks.size()));
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
