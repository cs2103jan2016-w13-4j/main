package command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Command class that represents the functionality of a command object.
 * @author leona_000
 *
 */
public class Command {

    private String action;
    private String task;
    private String deadline;
    private String startDateTime;
    private String endDateTime;
    private List<String> tags;

    public Command(String action, String task, String deadline, String startDateTime, String endDateTime, List<String> tags) {
        this.action = action;
        this.task = task;
        this.deadline = deadline;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.tags = new ArrayList<String>();
        tags.addAll(tags);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (!(obj instanceof Command)) {
            return false;
        }
        
        Command cmd = (Command) obj;
        
        return (cmd.action.equals(this.action) && cmd.deadline.equals(this.deadline) && cmd.task.equals(this.task) 
                && cmd.startDateTime.equals(this.startDateTime) && cmd.endDateTime.equals(this.endDateTime)
                && cmd.tags.equals(this.tags));
    }
}
