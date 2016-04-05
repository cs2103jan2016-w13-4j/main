// @@author A0130195M
package jfdi.logic.interfaces;

import jfdi.storage.apis.TaskAttributes;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * @author Liu Xinan
 */
public interface ILogic {

    /**
     * Handles user's input.
     * Called by UI whenever the user inputs a text command.
     *
     * @param input Input from user
     */
    void handleInput(String input);

    /**
     * Gets the list of command keywords.
     * Called by UI for populating auto-completion suggestion list.
     *
     * @return a {@code TreeSet} of {@code String}
     */
    TreeSet<String> getKeywords();

    /**
     * Gets the list of incomplete tasks.
     *
     * @return a {@code ArrayList} of {@code TaskAttributes}
     */
    ArrayList<TaskAttributes> getIncompleteTasks();

    /**
     * Gets the list of completed tasks.
     *
     * @return a {@code ArrayList} of {@code TaskAttributes}
     */
    ArrayList<TaskAttributes> getCompletedTasks();

    /**
     * Gets the list of all tasks.
     *
     * @return a {@code ArrayList} of {@code TaskAttributes}
     */
    ArrayList<TaskAttributes> getAllTasks();

    /**
     * Gets the list of upcoming tasks.
     *
     * @return a {@code ArrayList} of {@code TaskAttributes}
     */
    ArrayList<TaskAttributes> getUpcomingTasks();

    /**
     * Gets the list of overdue tasks.
     *
     * @return a {@code ArrayList} of {@code TaskAttributes}
     */
    ArrayList<TaskAttributes> getOverdueTasks();
}
