//@@author A0121621Y

package jfdi.storage.apis;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import jfdi.common.utilities.JfdiLogger;
import jfdi.storage.Constants;
import jfdi.storage.FileManager;
import jfdi.storage.IDatabase;
import jfdi.storage.entities.Task;
import jfdi.storage.exceptions.DuplicateTaskException;
import jfdi.storage.exceptions.FilePathPair;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.NoAttributesChangedException;
import jfdi.storage.serializer.Serializer;

/**
 * This class manages the entire collection of Tasks persisted in the program.
 *
 * @author Thng Kai Yuan
 *
 */
public class TaskDb implements IDatabase {

    // Singleton instance of TaskDb
    private static TaskDb instance = null;

    // All persisted Tasks
    private TreeMap<Integer, Task> taskList = null;

    // All deleted Tasks
    private TreeMap<Integer, Task> deletedTaskList = null;

    // The ID that will be assigned to the next new task
    private int nextId = 1;

    // The file path to the data file
    private Path filePath = null;

    // Logger for events
    private Logger logger = null;

    /**
     * This private constructor prevents more instances of TaskDb from being
     * created.
     */
    private TaskDb() {
        logger = JfdiLogger.getLogger();
        reset();
    }

    /**
     * This method returns the singleton instance of TaskDb.
     *
     * @return the singleton instance of TaskDb
     */
    public static TaskDb getInstance() {
        if (instance == null) {
            instance = new TaskDb();
        }
        return instance;
    }

    /**
     * This method persists the taskAttributes object either by creating the
     * relevant Task or updating the existing Task.
     *
     * @param taskAttributes
     *            the object containing the desired attributes of the task
     * @throws NoAttributesChangedException
     *             if the operation does not change any existing attributes
     * @throws InvalidIdException
     *             if the ID contained in the taskAttributes does not exist
     * @throws DuplicateTaskException
     *             if a task with the same attributes already exist
     */
    public void createOrUpdate(TaskAttributes taskAttributes) throws NoAttributesChangedException,
            InvalidIdException, DuplicateTaskException {
        assert taskAttributes != null;
        validateIsNotDuplicateTask(taskAttributes);
        Integer taskId = taskAttributes.getId();
        if (taskId != null) {
            update(taskAttributes);
        } else {
            create(taskAttributes);
        }
    }

    /**
     * This method validates that the given task attributes do not already exist
     * in the database. A TaskAttributes is not considered a duplicate if the
     * matching task is itself.
     *
     * @param taskAttributes
     *            the task attributes to be validated against existing tasks
     * @throws DuplicateTaskException
     *             if a task with similar attributes already exist in the
     *             database
     */
    private void validateIsNotDuplicateTask(TaskAttributes taskAttributes) throws DuplicateTaskException {
        if (taskList.values().stream()
                .anyMatch(task -> taskAttributes.similarTo(task) && !taskAttributes.equalTo(task))) {
            throw new DuplicateTaskException(taskAttributes);
        }
    }

    /**
     * This method creates a new Task from the given taskAttribute and persists
     * it to disk.
     *
     * @param taskAttributes
     *            the object containing the desired attributes of the task
     */
    private void create(TaskAttributes taskAttributes) {
        Task task = taskAttributes.toEntity();
        task.setId(nextId++);
        taskAttributes.setId(task.getId());
        taskList.put(task.getId(), task);
        persist();
        logger.fine(String.format(Constants.MESSAGE_LOG_CREATE_TASK, task.getId()));
    }

    /**
     * This method updates an existing Task object from its attributes object
     * and persists the updated Task to disk.
     *
     * @param taskAttributes
     *            the attributes object of the task
     * @throws NoAttributesChangedException
     *             if the taskAttributes object does not change the
     *             corresponding task
     * @throws InvalidIdException
     *             if the ID in taskAttributes does not exist
     */
    private void update(TaskAttributes taskAttributes) throws NoAttributesChangedException,
            InvalidIdException {
        Task task = getTaskById(taskAttributes.getId());
        assert task != null;
        validateAttributesHasChanged(taskAttributes, task);
        task.update(taskAttributes);
        persist();
        logger.fine(String.format(Constants.MESSAGE_LOG_UPDATE_TASK, task.getId()));
    }

    /**
     * This method validates that the given taskAttributes object has attributes
     * different from its corresponding Task in the database.
     *
     * @param taskAttributes
     *            the taskAttributes which have been changed
     * @param task
     *            the task that taskAttributes is to be compared with
     * @throws NoAttributesChangedException
     *             if no attributes have been changed
     */
    private void validateAttributesHasChanged(TaskAttributes taskAttributes, Task task)
            throws NoAttributesChangedException {
        if (taskAttributes.equalTo(task)) {
            throw new NoAttributesChangedException();
        }
    }

    /**
     * This method returns all the tasks (as a collection of their attributes)
     * currently persisted to Storage.
     *
     * @return a Collection of TaskAttributes
     */
    public Collection<TaskAttributes> getAll() {
        assert taskList != null;
        ArrayList<TaskAttributes> taskAttributes = new ArrayList<TaskAttributes>();
        for (Task task : taskList.values()) {
            taskAttributes.add(new TaskAttributes(task));
        }
        return taskAttributes;
    }

    /**
     * This method returns a collection of overdue TaskAttributes.
     *
     * @return a collection of overdue TaskAttributes
     */
    public Collection<TaskAttributes> getOverdue() {
        return getAll().stream().filter(TaskAttributes::isOverdue)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * This method returns a collection of upcoming TaskAttributes.
     *
     * @return a collection of upcoming TaskAttributes
     */
    public Collection<TaskAttributes> getUpcoming() {
        return getAll().stream().filter(TaskAttributes::isUpcoming)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * This method returns the TaskAttribute object of the task specified by the
     * ID.
     *
     * @param id
     *            the auto-assigned ID of each persisted Task
     * @return a TaskAttribute if the Task exists in Storage, null otherwise
     * @throws InvalidIdException
     *             if the given id does not exist in the database
     */
    public TaskAttributes getById(Integer id) throws InvalidIdException {
        assert id != null;
        Task task = getTaskById(id);
        return new TaskAttributes(task);
    }

    /**
     * This method returns the Task specified by its id, or null if the id is
     * invalid or the Task does not exist.
     *
     * @param id
     *            the ID of the task to be found
     * @return the Task object if the id is valid and the Task exists
     * @throws InvalidIdException
     *             if the given id does not exist in the database
     */
    private Task getTaskById(Integer id) throws InvalidIdException {
        if (taskList.get(id) == null) {
            throw new InvalidIdException(id);
        }
        return taskList.get(id);
    }

    /**
     * This method checks if a task (given by its ID) exists in the database.
     *
     * @param id
     *            the ID of the task to be checked
     * @return boolean indicating if the task exists in the database
     */
    public boolean hasId(int id) {
        return taskList.containsKey(id);
    }

    /**
     * This method marks a task (given by its ID) as completed.
     *
     * @param id
     *            id of the task to be marked
     * @throws NoAttributesChangedException
     *             if no attributes have been changed
     * @throws InvalidIdException
     *             if the given id is invalid
     */
    public void markAsComplete(Integer id) throws NoAttributesChangedException, InvalidIdException {
        assert id != null;
        TaskAttributes taskAttributes = getById(id);
        taskAttributes.setCompleted(true);
        update(taskAttributes);
    }

    /**
     * This method marks a task (given by its ID) as incomplete.
     *
     * @param id
     *            id of the task to be marked
     * @throws NoAttributesChangedException
     *             if no attributes have been changed
     * @throws InvalidIdException
     *             if the given id is invalid
     */
    public void markAsIncomplete(Integer id) throws NoAttributesChangedException, InvalidIdException {
        assert id != null;
        TaskAttributes taskAttributes = getById(id);
        taskAttributes.setCompleted(false);
        update(taskAttributes);
    }

    /**
     * This method soft-deletes the task given by the ID.
     *
     * @param id
     *            the task to be deleted
     * @throws InvalidIdException
     *             if the given id does not exist in the database
     */
    public void destroy(Integer id) throws InvalidIdException {
        assert id != null;
        Task task = getTaskById(id);
        assert task != null;
        softDelete(id);
        persist();
        logger.fine(String.format(Constants.MESSAGE_LOG_DELETE_TASK, id));
    }

    /**
     * This method transfers a task from the task list to the deleted task list.
     *
     * @param id
     *            the ID of the task that is to be moved
     */
    private void softDelete(Integer id) {
        Task task = taskList.remove(id);
        deletedTaskList.put(task.getId(), task);
    }

    /**
     * This method recovers the task given by the ID.
     *
     * @param id
     *            the task to be recovered
     * @throws InvalidIdException
     *             if the specified ID does not exist in the deleted list
     * @throws DuplicateTaskException
     *             if a task with similar attributes already exists in the database
     */
    public void undestroy(Integer id) throws InvalidIdException, DuplicateTaskException {
        assert id != null;
        if (!deletedTaskList.containsKey(id)) {
            throw new InvalidIdException(id);
        }
        undelete(id);
        persist();
        logger.fine(String.format(Constants.MESSAGE_LOG_RECOVER_TASK, id));
    }

    /**
     * This method transfers a task from the deleted task list to the task list.
     *
     * @param id
     *            the ID of the task that is to be moved
     * @throws DuplicateTaskException
     *             if a task with similar attributes already exists in the database
     */
    private void undelete(Integer id) throws DuplicateTaskException {
        TaskAttributes taskAttributes = new TaskAttributes(deletedTaskList.get(id));
        validateIsNotDuplicateTask(taskAttributes);
        Task task = deletedTaskList.remove(id);
        taskList.put(task.getId(), task);
    }

    /**
     * This method persists all existing tasks to the file system.
     */
    public void persist() {
        String json = Serializer.serialize(taskList.values());
        FileManager.writeToFile(json, filePath);
    }

    /**
     * This method resets the program's internal storage of tasks. This method
     * should only be used by the public in tests.
     */
    public void reset() {
        taskList = new TreeMap<Integer, Task>();
        deletedTaskList = new TreeMap<Integer, Task>();
        nextId = 1;
    }

    /**
     * This method sets the filepath of the record using absoluteFolderPath to
     * store the record's data file.
     *
     * @param absoluteFolderPath
     *            the directory that should contain the data file
     */
    public void setFilePath(String absoluteFolderPath) {
        assert absoluteFolderPath != null;
        filePath = Paths.get(absoluteFolderPath, Constants.FILENAME_TASK);
    }

    /**
     * @return the path of the record as set using the setFilePath method
     */
    public Path getFilePath() {
        return filePath;
    }

    /**
     * This method loads/refreshes the record (taskList) based on data contained
     * within the data file of the record.
     *
     * @return FilePathPair if a file was replaced, null otherwise
     */
    public FilePathPair load() {
        if (!Files.exists(filePath)) {
            return null;
        }

        String persistedJsonData = FileManager.readFileToString(filePath);
        Task[] taskArray = Serializer.deserialize(persistedJsonData, Task[].class);
        taskArray = validateTaskArray(taskArray);

        if (taskArray == null) {
            String movedTo = FileManager.backupAndRemove(filePath);
            return new FilePathPair(filePath.toString(), movedTo);
        } else if (taskArray.length > 0) {
            populateTaskList(taskArray);
            nextId = taskList.lastKey() + 1;
        }
        return null;
    }

    /**
     * This method validates an entire array of tasks. If any of the tasks
     * within are invalid, null is returned. Otherwise, the original array is
     * returned.
     *
     * @param taskArray
     *            the array that is to be validated
     * @return taskArray if the entire array of tasks are valid, null otherwise
     */
    private Task[] validateTaskArray(Task[] taskArray) {
        if (taskArray == null) {
            return null;
        }

        for (Task task : taskArray) {
            if (!(new TaskAttributes(task).isValid())) {
                return null;
            }
        }
        return taskArray;
    }

    /**
     * This method populates the task list with tasks given in the taskArray.
     *
     * @param taskArray
     *            the array of tasks that we want to populate the program with
     */
    private void populateTaskList(Task[] taskArray) {
        reset();
        for (Task task : taskArray) {
            task.setId(nextId++);
            taskList.put(task.getId(), task);
        }
    }

}
