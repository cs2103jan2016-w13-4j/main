package jfdi.storage.records;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
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
    private static TreeMap<Integer, Task> taskList = new TreeMap<Integer, Task>();
    // All deleted Tasks
    private static TreeMap<Integer, Task> deletedTaskList = new TreeMap<Integer, Task>();
    // The ID that will be assigned to the next new task
    private static int nextId = 1;
    // The filepath to the data file
    private static Path filePath = null;

    // Attributes of a Task object
    private Integer id = null;
    private String description = null;
    private LocalDateTime startDateTime = null;
    private LocalDateTime endDateTime = null;
    private HashSet<String> tags = null;
    private TreeSet<Duration> reminders = null;
    private boolean completed = false;

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
        if (taskArray != null) {
            populateTaskList(taskArray);
            nextId = taskList.lastKey() + 1;
            return null;
        }

        String movedTo = FileManager.backupAndRemove(filePath);
        return new FilePathPair(filePath.toString(), movedTo);
    }

    private static void populateTaskList(Task[] taskArray) {
        for (Task task : taskArray) {
            taskList.put(task.getId(), task);
        }
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
        return taskList.get(id);
    }

    public static boolean markAsComplete(Integer id) {
        if (!taskList.containsKey(id)) {
            return false;
        }

        Task task = taskList.get(id);
        if (task.isCompleted()) {
            return false;
        }

        task.setCompleted(true);
        return true;
    }

    public static boolean markAsIncomplete(Integer id) {
        if (!taskList.containsKey(id)) {
            return false;
        }

        Task task = taskList.get(id);
        if (!task.isCompleted()) {
            return false;
        }

        task.setCompleted(false);
        return true;
    }

    public static boolean destroy(Integer id) {
        if (taskList.containsKey(id)) {
            softDelete(id);
            return true;
        }

        return false;
    }

    public static boolean undestroy(Integer id) {
        if (deletedTaskList.containsKey(id)) {
            undelete(id);
            return true;
        }

        return false;
    }

    public static void persist() {
        String json = Serializer.serialize(taskList.values());
        FileManager.writeToFile(json, filePath);
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the startDateTime
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * @param startDateTime
     *            the startDateTime to set
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * @return the endDateTime
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * @param endDateTime
     *            the endDateTime to set
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * @return the tags
     */
    public HashSet<String> getTags() {
        return tags;
    }

    /**
     * @param tags
     *            the tags to set
     */
    public void setTags(HashSet<String> tags) {
        this.tags = tags;
    }

    /**
     * @return the reminders
     */
    public TreeSet<Duration> getReminders() {
        return reminders;
    }

    /**
     * @param reminders
     *            the reminders to set
     */
    public void setReminders(TreeSet<Duration> reminders) {
        this.reminders = reminders;
    }

    /**
     * @return the completed
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * @param completed
     *            the completed to set
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean createAndPersist() {
        if (id != null) {
            return false;
        }

        id = nextId++;
        taskList.put(this.getId(), this);
        Task.persist();
        return true;
    }

    private static void softDelete(Integer id) {
        Task task = taskList.remove(id);
        deletedTaskList.put(task.getId(), task);
    }

    private static void undelete(Integer id) {
        Task task = deletedTaskList.remove(id);
        taskList.put(task.getId(), task);
    }
}
