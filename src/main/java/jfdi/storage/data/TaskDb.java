package jfdi.storage.data;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

import jfdi.storage.Constants;
import jfdi.storage.FileManager;
import jfdi.storage.exceptions.FilePathPair;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.NoAttributesChangedException;
import jfdi.storage.serializer.Serializer;

public class TaskDb {

    // All persisted Tasks
    private static TreeMap<Integer, Task> taskList = null;

    // All deleted Tasks
    private static TreeMap<Integer, Task> deletedTaskList = null;

    // The ID that will be assigned to the next new task
    private static int nextId = 1;

    // The file path to the data file
    private static Path filePath = null;

    static {
        if (taskList == null) {
            resetProgramStorage();
        }
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
     */
    public static void createOrUpdate(TaskAttributes taskAttributes) throws NoAttributesChangedException,
    InvalidIdException {
        assert taskAttributes != null;
        Integer taskId = taskAttributes.getId();
        if (taskId != null) {
            update(taskAttributes);
        } else {
            create(taskAttributes);
        }
    }

    /**
     * This method creates a new Task from the given taskAttribute and persists
     * it to disk.
     *
     * @param taskAttributes
     *            the object containing the desired attributes of the task
     */
    private static void create(TaskAttributes taskAttributes) {
        assert taskAttributes.getId() == null;
        Task task = taskAttributes.toEntity();
        task.setId(nextId++);
        taskAttributes.setId(task.getId());
        taskList.put(task.getId(), task);
        persist();
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
    private static void update(TaskAttributes taskAttributes) throws NoAttributesChangedException,
    InvalidIdException {
        Task task = TaskDb.getTaskById(taskAttributes.getId());
        assert task != null;
        validateAttributesHasChanged(taskAttributes, task);
        task.update(taskAttributes);
        persist();
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
    private static void validateAttributesHasChanged(TaskAttributes taskAttributes, Task task)
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
    public static Collection<TaskAttributes> getAll() {
        ArrayList<TaskAttributes> taskAttributes = new ArrayList<TaskAttributes>();
        for (Task task : taskList.values()) {
            taskAttributes.add(new TaskAttributes(task));
        }
        return taskAttributes;
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
    public static TaskAttributes getById(Integer id) throws InvalidIdException {
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
    private static Task getTaskById(Integer id) throws InvalidIdException {
        if (id == null || taskList.get(id) == null) {
            throw new InvalidIdException(id);
        }
        return taskList.get(id);
    }

    /**
     * This method returns an ArrayList of TaskAttributes that correspond to
     * Tasks containing the specified tag in storage.
     *
     * @param tag
     *            the search tag
     * @return an ArrayList of TaskAttributes corresponding to Tasks that
     *         contains the specified tag
     */
    public static ArrayList<TaskAttributes> getByTag(String tag) {
        assert tag != null;

        ArrayList<TaskAttributes> taskAttributesList = new ArrayList<TaskAttributes>();
        for (TaskAttributes taskAttributes : TaskDb.getAll()) {
            if (taskAttributes.hasTag(tag)) {
                taskAttributesList.add(taskAttributes);
            }
        }
        return taskAttributesList;
    }

    /**
     * This method checks if a task (given by its ID) exists in the database.
     *
     * @param id
     *            the ID of the task to be checked
     * @return boolean indicating if the task exists in the database
     */
    public static boolean hasId(int id) {
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
    public static void markAsComplete(Integer id) throws NoAttributesChangedException, InvalidIdException {
        TaskAttributes taskAttributes = TaskDb.getById(id);
        taskAttributes.setCompleted(true);
        TaskDb.update(taskAttributes);
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
    public static void markAsIncomplete(Integer id) throws NoAttributesChangedException, InvalidIdException {
        TaskAttributes taskAttributes = TaskDb.getById(id);
        taskAttributes.setCompleted(false);
        TaskDb.update(taskAttributes);
    }

    /**
     * This method soft-deletes the task given by the ID.
     *
     * @param id
     *            the task to be deleted
     * @throws InvalidIdException
     *             if the given id does not exist in the database
     */
    public static void destroy(Integer id) throws InvalidIdException {
        Task task = TaskDb.getTaskById(id);
        assert task != null;
        softDelete(id);
        persist();
    }

    /**
     * This method transfers a task from the task list to the deleted task list.
     *
     * @param id
     *            the ID of the task that is to be moved
     */
    private static void softDelete(Integer id) {
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
     */
    public static void undestroy(Integer id) throws InvalidIdException {
        assert id != null;
        if (!deletedTaskList.containsKey(id)) {
            throw new InvalidIdException(id);
        }
        undelete(id);
        persist();
    }

    /**
     * This method transfers a task from the deleted task list to the task list.
     *
     * @param id
     *            the ID of the task that is to be moved
     */
    private static void undelete(Integer id) {
        Task task = deletedTaskList.remove(id);
        taskList.put(task.getId(), task);
    }

    /**
     * This method adds the given reminder to a task with the given id.
     *
     * @param id
     *            the id of the task that we want to add the reminder to
     * @param reminder
     *            the reminder that we want to add to the task
     * @throws NoAttributesChangedException
     *             if no attributes have been changed
     * @throws InvalidIdException
     *             if the given id is invalid
     */
    public static void addReminderById(Integer id, Duration reminder) throws InvalidIdException,
    NoAttributesChangedException {
        assert reminder != null;
        TaskAttributes taskAttributes = TaskDb.getById(id);
        taskAttributes.addReminder(reminder);
        TaskDb.update(taskAttributes);
    }

    /**
     * This method adds the given tag to a task with the given id.
     *
     * @param id
     *            the id of the task that we want to add the tag to
     * @param tag
     *            the tag that we want to add to the task
     * @throws NoAttributesChangedException
     *             if no attributes have been changed
     * @throws InvalidIdException
     *             if the given id is invalid
     */
    public static void addTagById(Integer id, String tag) throws NoAttributesChangedException,
    InvalidIdException {
        assert tag != null;
        TaskAttributes taskAttributes = TaskDb.getById(id);
        taskAttributes.addTag(tag);
        TaskDb.update(taskAttributes);
    }

    /**
     * This method removes the given tag from a task with the given id.
     *
     * @param id
     *            the id of the task that we want to remove the tag from
     * @param tag
     *            the tag that we want to remove from the task
     * @throws NoAttributesChangedException
     *             if no attributes have been changed
     * @throws InvalidIdException
     *             if the given id is invalid
     */
    public static void removeTagById(Integer id, String tag) throws NoAttributesChangedException,
    InvalidIdException {
        assert tag != null;
        TaskAttributes taskAttributes = TaskDb.getById(id);
        taskAttributes.removeTag(tag);
        TaskDb.update(taskAttributes);
    }

    /**
     * This method persists all existing tasks to the file system.
     */
    public static void persist() {
        String json = Serializer.serialize(taskList.values());
        FileManager.writeToFile(json, filePath);
    }

    /**
     * This method resets the program's internal storage of tasks. This method
     * should only be used by the public in tests.
     */
    public static void resetProgramStorage() {
        taskList = new TreeMap<Integer, Task>();
        deletedTaskList = new TreeMap<Integer, Task>();
        nextId = 1;
    }

    /**
     * This method checks if the program's internal storage of tasks is empty.
     * Used by UI to decided whether to display "Welcome" or ERROR.
     */
    public static boolean isEmptyStorage() {
        return taskList.isEmpty();
    }

    /**
     * This method sets the filepath of the record using absoluteFolderPath to
     * store the record's data file.
     *
     * @param absoluteFolderPath
     *            the directory that should contain the data file
     */
    public static void setFilePath(String absoluteFolderPath) {
        filePath = Paths.get(absoluteFolderPath, Constants.FILENAME_TASK);
    }

    /**
     * @return the path of the record as set using the setFilePath method
     */
    public static Path getFilePath() {
        return filePath;
    }

    /**
     * This method loads/refreshes the record (taskList) based on data contained
     * within the data file of the record.
     *
     * @return FilePathPair if a file was replaced, null otherwise
     */
    public static FilePathPair load() {
        File dataFile = filePath.toFile();
        if (!dataFile.exists()) {
            return null;
        }

        String persistedJsonData = FileManager.readFileToString(filePath);
        Task[] taskArray = Serializer.deserialize(persistedJsonData, Task[].class);

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
     * This method populates the task list with tasks given in the taskArray.
     *
     * @param taskArray
     *            the array of tasks that we want to populate the program with
     */
    private static void populateTaskList(Task[] taskArray) {
        resetProgramStorage();
        for (Task task : taskArray) {
            taskList.put(nextId++, task);
        }
    }

}
