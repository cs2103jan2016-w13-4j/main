package jfdi.storage.records;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

import jfdi.storage.Constants;
import jfdi.storage.FileManager;
import jfdi.storage.exceptions.FilePathPair;
import jfdi.storage.serializer.Serializer;

public class Task {

    // All persisted Tasks
    private static TreeMap<Integer, Task> taskList = null;
    // All deleted Tasks
    private static TreeMap<Integer, Task> deletedTaskList = null;
    // The ID that will be assigned to the next new task
    private static int nextId = 1;
    // The filepath to the data file
    private static Path filePath = null;

    // Attributes of a Task object
    private Integer id = null;
    private String description = null;
    private LocalDateTime startDateTime = null;
    private LocalDateTime endDateTime = null;
    private HashSet<String> tags = new HashSet<String>();
    private TreeSet<Duration> reminders = new TreeSet<Duration>();
    private boolean isCompleted = false;

    static {
        if (taskList == null) {
            resetProgramStorage();
        }
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
     * This method returns all the tasks currently persisted to Storage.
     *
     * @return a Collection of tasks currently persisted to Storage
     */
    public static Collection<Task> getAll() {
        return taskList.values();
    }

    /**
     * This method returns a Task object specified by its ID.
     *
     * @param id
     *            the auto-assigned ID of each persisted Task
     * @return the Task if it exists in Storage, null otherwise
     */
    public static Task getById(Integer id) {
        if (id == null) {
            return null;
        }

        return taskList.get(id);
    }

    /**
     * This method returns an ArrayList of tasks that contain the specified tag.
     *
     * @param tag
     *            the search tag
     * @return an ArrayList of tasks that contains the specified tag, or null if
     *         tag is null
     */
    public static ArrayList<Task> getByTag(String tag) {
        if (tag == null) {
            return null;
        }

        ArrayList<Task> taskList = new ArrayList<Task>();
        for (Task task : Task.getAll()) {
            if (task.hasTag(tag)) {
                taskList.add(task);
            }
        }

        return taskList;
    }

    /**
     * This method marks a task (given by its ID) as completed.
     *
     * @param id
     *          id of the task to be marked
     * @return boolean indicating if the status of the task was changed
     */
    public static boolean markAsComplete(Integer id) {
        Task task = Task.getById(id);
        if (task == null) {
            return false;
        }

        if (task.isCompleted()) {
            return false;
        }

        task.setCompleted(true);
        return true;
    }

    /**
     * This method marks a task (given by its ID) as incomplete.
     *
     * @param id
     *          id of the task to be marked
     * @return boolean indicating if the status of the task was changed
     */
    public static boolean markAsIncomplete(Integer id) {
        Task task = Task.getById(id);
        if (task == null) {
            return false;
        }

        if (!task.isCompleted()) {
            return false;
        }

        task.setCompleted(false);
        return true;
    }

    /**
     * This method soft-deletes the task given by the ID.
     *
     * @param id
     *          the task to be deleted
     * @return boolean indicating if the task was deleted
     */
    public static boolean destroy(Integer id) {
        Task task = Task.getById(id);
        if (task != null) {
            softDelete(id);
            return true;
        }

        return false;
    }

    /**
     * This method recovers the task given by the ID.
     *
     * @param id
     *          the task to be recovered
     * @return boolean indicating if the task was recovered
     */
    public static boolean undestroy(Integer id) {
        if (id != null && deletedTaskList.containsKey(id)) {
            undelete(id);
            return true;
        }

        return false;
    }

    /**
     * This method adds the given reminder to a task with the given id.
     *
     * @param id
     *            the id of the task that we want to add the reminder to
     * @param reminder
     *            the reminder that we want to add to the task
     * @return boolean indicating if the reminder was added to the task with the
     *         given id
     */
    public static boolean addReminderById(Integer id, Duration reminder) {
        Task task = Task.getById(id);
        if (task == null) {
            return false;
        }

        return task.addReminder(reminder);
    }

    /**
     * This method adds the given tag to a task with the given id.
     *
     * @param id
     *            the id of the task that we want to add the tag to
     * @param tag
     *            the tag that we want to add to the task
     * @return boolean indicating if the tag was added to the task with the
     *         given id
     */
    public static boolean addTagById(Integer id, String tag) {
        Task task = Task.getById(id);
        if (task == null) {
            return false;
        }

        return task.addTag(tag);
    }

    /**
     * This method removes the given tag from a task with the given id.
     *
     * @param id
     *            the id of the task that we want to remove the tag from
     * @param tag
     *            the tag that we want to remove from the task
     * @return boolean indicating if the tag was removed from the task with the
     *         given id
     */
    public static boolean removeTagById(Integer id, String tag) {
        Task task = Task.getById(id);
        if (task == null) {
            return false;
        }

        return task.removeTag(tag);
    }

    /**
     * This method persists all existing tasks to the file system.
     */
    public static void persist() {
        String json = Serializer.serialize(taskList.values());
        FileManager.writeToFile(json, filePath);
    }

    /**
     * This method adds a task to the program's internal storage and assigns it
     * an ID. All task data is then persisted to the file system.
     *
     * @return boolean indicating if the task was added and persisted
     */
    public boolean createAndPersist() {
        if (id != null) {
            return false;
        }

        id = nextId++;
        taskList.put(this.getId(), this);
        Task.persist();
        return true;
    }


    /*
     * Getters and setters
     */

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public HashSet<String> getTags() {
        return tags;
    }

    public void setTags(String... tags) {
        this.tags = new HashSet<String>();
        for (String tag : tags) {
            this.addTag(tag);
        }
    }

    public TreeSet<Duration> getReminders() {
        return reminders;
    }

    public void setReminders(TreeSet<Duration> reminders) {
        this.reminders = reminders;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }


    /*
     * Private helper methods
     */

    private static void softDelete(Integer id) {
        Task task = taskList.remove(id);
        deletedTaskList.put(task.getId(), task);
    }

    private static void undelete(Integer id) {
        Task task = deletedTaskList.remove(id);
        taskList.put(task.getId(), task);
    }

    /**
     * This method populates the task list with tasks given in the taskArray.
     *
     * @param taskArray
     *          the array of tasks that we want to populate the program with
     */
    private static void populateTaskList(Task[] taskArray) {
        resetProgramStorage();
        for (Task task : taskArray) {
            taskList.put(nextId++, task);
        }
    }

    /**
     * This method removes a given tag from the task.
     *
     * @param tag
     *            the tag that is to be removed
     * @return boolean indicating if the tag was removed
     */
    private boolean removeTag(String tag) {
        HashSet<String> tags = this.getTags();
        return tags.remove(tag);
    }

    /**
     * This method adds a given tag to the task.
     *
     * @param tag
     *            the tag that is to be added
     * @return boolean indicating if the tag was added
     */
    private boolean addTag(String tag) {
        HashSet<String> tags = this.getTags();
        return tags.add(tag);
    }

    /**
     * This method checks if the task contains the given tag.
     *
     * @param tag
     *            the tag to be searched for
     * @return boolean indicating if the task contains the given tag
     */
    private boolean hasTag(String tag) {
        HashSet<String> tags = this.getTags();
        return tags.contains(tag);
    }

    /**
     * This method adds a given reminder to the task.
     *
     * @param reminder
     *          the reminder that is to be added to the task
     * @return boolean indicating if the reminder was added to the task
     */
    private boolean addReminder(Duration reminder) {
        TreeSet<Duration> reminders = this.getReminders();
        return reminders.add(reminder);
    }
}
