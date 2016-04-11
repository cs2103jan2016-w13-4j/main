// @@author A0130195M

package jfdi.logic.commands;

import jfdi.logic.events.NoSurpriseEvent;
import jfdi.logic.events.SurpriseEvent;
import jfdi.logic.interfaces.Command;
import jfdi.storage.apis.TaskAttributes;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author Liu Xinan
 */
public class WildcardCommand extends Command {

    private TaskAttributes lucky;

    private WildcardCommand(Builder builder) {}

    public static class Builder {

        public WildcardCommand build() {
            return new WildcardCommand(this);
        }

    }

    public TaskAttributes getLucky() {
        return lucky;
    }

    @Override
    public void execute() {
        ArrayList<TaskAttributes> incompleteTasks = taskDb.getAll().stream()
                .filter(task -> !task.isCompleted() && task.getStartDateTime() == null)
                .collect(Collectors.toCollection(ArrayList::new));

        SecureRandom random = new SecureRandom();

        if (!incompleteTasks.isEmpty()) {
            lucky = incompleteTasks.get(random.nextInt(incompleteTasks.size()));
            eventBus.post(new SurpriseEvent(lucky));

            logger.info("Lucky task chosen: " + lucky.getDescription());
        } else {
            eventBus.post(new NoSurpriseEvent(NoSurpriseEvent.Error.NO_TASKS));

            logger.warning("No surprise for everybody!");
        }
    }

    @Override
    public void undo() {
        assert false;
    }

}
