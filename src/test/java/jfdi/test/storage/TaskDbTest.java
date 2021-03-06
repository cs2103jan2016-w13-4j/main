//@@author A0121621Y

package jfdi.test.storage;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;

import jfdi.storage.Constants;
import jfdi.storage.apis.MainStorage;
import jfdi.storage.apis.TaskAttributes;
import jfdi.storage.apis.TaskDb;
import jfdi.storage.exceptions.FilePathPair;
import jfdi.storage.exceptions.InvalidIdException;
import jfdi.storage.exceptions.DuplicateTaskException;
import jfdi.storage.exceptions.NoAttributesChangedException;
import jfdi.storage.serializer.Serializer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TaskDbTest {

    private static Path testDirectory = null;
    private static String testDirectoryString = null;
    private static TaskDb taskDbInstance = null;
    private static MainStorage mainStorageInstance = null;
    private static String originalPreference = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        testDirectory = Files.createTempDirectory(Constants.TEST_DIRECTORY_NAME);
        testDirectoryString = testDirectory.toString();
        mainStorageInstance = MainStorage.getInstance();
        originalPreference = mainStorageInstance.getPreferredDirectory();
        mainStorageInstance.setPreferredDirectory(testDirectoryString);
        mainStorageInstance.initialize();
        mainStorageInstance.use(testDirectoryString);
        taskDbInstance = TaskDb.getInstance();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        TestHelper.revertOriginalPreference(mainStorageInstance, originalPreference);
    }

    @After
    public void tearDown() throws Exception {
        taskDbInstance.reset();
    }

    @Test
    public void testCreate() throws Exception {
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);

        // The database should be empty prior to the task creation
        assertTrue(taskDbInstance.getAll().isEmpty());

        // Command under test
        taskDbInstance.createOrUpdate(taskAttributes);

        // Check that the task has been created
        assertEquals(1, taskDbInstance.getAll().size());
        assertEquals(Constants.TEST_TASK_DESCRIPTION_1,
                taskDbInstance.getById(taskAttributes.getId()).getDescription());
    }

    @Test(expected = DuplicateTaskException.class)
    public void testCreateDuplicate() throws Exception {
        // Create the first Task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.setStartDateTime(Constants.TEST_TASK_STARTDATETIME);
        taskAttributes.setEndDateTime(Constants.TEST_TASK_ENDDATETIME);
        taskDbInstance.createOrUpdate(taskAttributes);

        // Create another duplicate Task
        // This should trigger the exception
        taskAttributes.setId(null);
        taskDbInstance.createOrUpdate(taskAttributes);
    }

    @Test
    public void testUpdate() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskDbInstance.createOrUpdate(taskAttributes);

        // Check that the task has been created
        assertEquals(1, taskDbInstance.getAll().size());
        assertEquals(Constants.TEST_TASK_DESCRIPTION_1, taskDbInstance.getById(1).getDescription());

        // Command under test (update the task's description)
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_2);
        taskDbInstance.createOrUpdate(taskAttributes);

        // Ensure that object update (and not creation) takes place
        assertEquals(1, taskDbInstance.getAll().size());
        assertEquals(Constants.TEST_TASK_DESCRIPTION_2,
                taskDbInstance.getById(taskAttributes.getId()).getDescription());
    }

    @Test(expected = NoAttributesChangedException.class)
    public void testNoChangesUpdate() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskDbInstance.createOrUpdate(taskAttributes);

        // The second no-changes update triggers the exception
        taskDbInstance.createOrUpdate(taskAttributes);
    }

    @Test(expected = InvalidIdException.class)
    public void testInvalidIdUpdate() throws Exception {
        // Set the ID of a TaskAttributes to an invalid one
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.setId(1);

        // Command under test (invalid ID will trigger exception)
        taskDbInstance.createOrUpdate(taskAttributes);
    }

    @Test
    public void testSetAndGetFilePath() {
        // Save the original file path so that we can revert it later
        Path originalFilePath = taskDbInstance.getFilePath();
        Path subdirectory = Paths.get(testDirectory.toString(), Constants.TEST_SUBDIRECTORY_NAME);
        Path expectedTaskPath = Paths.get(subdirectory.toString(), Constants.FILENAME_TASK);

        // Test setFilePath
        taskDbInstance.setFilePath(subdirectory.toString());

        // Assert that we get back the same file path
        assertEquals(expectedTaskPath, taskDbInstance.getFilePath());

        // Reset back to the original file path
        taskDbInstance.setFilePath(originalFilePath.getParent().toString());
    }

    @Test
    public void testGetAll() throws Exception {
        // Create the first task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // A hack to create another task
        taskAttributes.setId(null);
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_2);
        taskAttributes.save();

        // Check that the stored tasks are as expected
        ArrayList<TaskAttributes> taskAttributesList = new ArrayList<TaskAttributes>(taskDbInstance.getAll());
        assertEquals(2, taskAttributesList.size());
        assertEquals(Constants.TEST_TASK_DESCRIPTION_1, taskAttributesList.get(0).getDescription());
        assertEquals(Constants.TEST_TASK_DESCRIPTION_2, taskAttributesList.get(1).getDescription());
    }

    @Test
    public void testGetOverdueAndGetUpcoming() throws Exception {
        // Task 1 has start date-time which is overdue
        TaskAttributes task1 = new TaskAttributes();
        task1.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task1.setStartDateTime(LocalDateTime.now().minusDays(1));
        task1.save();

        // Task 2 has start date-time which is upcoming
        TaskAttributes task2 = new TaskAttributes();
        task2.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task2.setStartDateTime(LocalDateTime.now().plusDays(1));
        task2.save();

        // Task 3 has end date-time which is overdue
        TaskAttributes task3 = new TaskAttributes();
        task3.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task3.setEndDateTime(LocalDateTime.now().minusDays(1));
        task3.save();

        // Task 4 has end date-time which is upcoming
        TaskAttributes task4 = new TaskAttributes();
        task4.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task4.setEndDateTime(LocalDateTime.now().plusDays(1));
        task4.save();

        // Task 5 is an event which started 1 day ago (overdue)
        TaskAttributes task5 = new TaskAttributes();
        task5.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task5.setStartDateTime(LocalDateTime.now().minusDays(1));
        task5.setEndDateTime(LocalDateTime.now().plusDays(1));
        task5.save();

        // Task 6 is an event which will start 1 day later (upcoming)
        TaskAttributes task6 = new TaskAttributes();
        task6.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        task6.setStartDateTime(LocalDateTime.now().plusDays(1));
        task6.setEndDateTime(LocalDateTime.now().plusDays(2));
        task6.save();

        // Set the expected overdue IDs
        ArrayList<Integer> overdueIds = new ArrayList<Integer>();
        overdueIds.add(task1.getId());
        overdueIds.add(task3.getId());
        overdueIds.add(task5.getId());

        // Set the expected upcoming IDs
        ArrayList<Integer> upcomingIds = new ArrayList<Integer>();
        upcomingIds.add(task2.getId());
        upcomingIds.add(task4.getId());
        upcomingIds.add(task6.getId());

        // Check that we have the correct set of overdue/upcoming tasks
        assertTrue(taskDbInstance.getOverdue().stream().allMatch(task -> overdueIds.contains(task.getId())));
        assertTrue(taskDbInstance.getUpcoming().stream().allMatch(task -> upcomingIds.contains(task.getId())));

        // Mark all tasks as completed
        for (TaskAttributes task : taskDbInstance.getAll()) {
            task.setCompleted(true);
            task.save();
        }

        // There shouldn't be anymore overdue or upcoming tasks
        assertEquals(0, taskDbInstance.getOverdue().size());
        assertEquals(0, taskDbInstance.getUpcoming().size());
    }

    @Test
    public void testGetById() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Check that we can get the same task using getById
        assertEquals(Constants.TEST_TASK_DESCRIPTION_1,
                taskDbInstance.getById(taskAttributes.getId()).getDescription());
    }

    @Test
    public void testHasId() throws Exception {
        // No task yet, so any ID should be invalid
        assertFalse(taskDbInstance.hasId(1));

        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Now taskAttribute's ID should exist
        assertTrue(taskDbInstance.hasId(taskAttributes.getId()));
    }

    @Test
    public void testMarkAsCompleteAndIncomplete() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Test markAsComplete
        taskDbInstance.markAsComplete(taskAttributes.getId());
        assertTrue(taskDbInstance.getById(taskAttributes.getId()).isCompleted());

        // Test markAsIncomplete
        taskDbInstance.markAsIncomplete(1);
        assertFalse(taskDbInstance.getById(taskAttributes.getId()).isCompleted());
    }

    @Test(expected = NoAttributesChangedException.class)
    public void testMarkCompletedAsComplete() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Mark the task as completed
        taskAttributes.setCompleted(true);
        taskAttributes.save();

        // Mark it as complete again (this should trigger an exception)
        taskDbInstance.markAsComplete(taskAttributes.getId());
    }

    @Test(expected = NoAttributesChangedException.class)
    public void testMarkIncompletedAsIncomplete() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Mark an incomplete task as incomplete (this should trigger an exception)
        taskDbInstance.markAsIncomplete(taskAttributes.getId());
    }

    @Test(expected = InvalidIdException.class)
    public void testMarkInvalidIdAsComplete() throws Exception {
        // Mark an invalid ID as complete
        taskDbInstance.markAsComplete(Integer.MAX_VALUE);
    }

    @Test(expected = InvalidIdException.class)
    public void testMarkInvalidIdAsIncomplete() throws Exception {
        // Mark an invalid ID as incomplete
        taskDbInstance.markAsIncomplete(Integer.MAX_VALUE);
    }

    @Test
    public void testDestroyAndUndestroy() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Check that it has been created
        assertEquals(1, taskDbInstance.getAll().size());

        // Destroy the task
        taskDbInstance.destroy(taskAttributes.getId());
        assertEquals(0, taskDbInstance.getAll().size());

        // Undestroy the task
        taskDbInstance.undestroy(taskAttributes.getId());
        assertEquals(1, taskDbInstance.getAll().size());
    }

    @Test(expected = InvalidIdException.class)
    public void testInvalidIdDestroy() throws Exception {
        // Destroy an invalid ID
        taskDbInstance.destroy(Integer.MAX_VALUE);
    }

    @Test(expected = InvalidIdException.class)
    public void testInvalidIdUndestroy() throws Exception {
        // Undestroy an invalid ID
        taskDbInstance.undestroy(Integer.MAX_VALUE);
    }

    @Test(expected = DuplicateTaskException.class)
    public void testUndestroyDuplicateTask() throws Exception {
        // Create the first Task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.setStartDateTime(Constants.TEST_TASK_STARTDATETIME);
        taskAttributes.setEndDateTime(Constants.TEST_TASK_ENDDATETIME);
        taskDbInstance.createOrUpdate(taskAttributes);
        int firstTaskId = taskAttributes.getId();

        // Destroy the first task
        taskDbInstance.destroy(firstTaskId);

        // Create another duplicate Task
        taskAttributes.setId(null);
        taskDbInstance.createOrUpdate(taskAttributes);

        // Undestroy the first task (it becomes a duplicate now)
        // This should trigger the exception
        taskDbInstance.undestroy(firstTaskId);
    }

    @Test
    public void testLoad() throws Exception {
        // Create a task
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Get the JSON form of the current state
        String json = Serializer.serialize(taskDbInstance.getAll());

        // Remove the task
        taskDbInstance.destroy(taskAttributes.getId());
        assertTrue(taskDbInstance.getAll().isEmpty());

        // Create the data file and load from it
        TestHelper.createTaskFileWith(testDirectoryString, json);
        FilePathPair filesReplaced = taskDbInstance.load();

        // No files should have been replaced
        assertNull(filesReplaced);

        // Check that the original task exists after load
        assertEquals(1, taskDbInstance.getAll().size());
        TaskAttributes retrievedTaskAttributes = taskDbInstance.getById(taskAttributes.getId());
        assertEquals(Constants.TEST_TASK_DESCRIPTION_1, retrievedTaskAttributes.getDescription());
    }

    @Test
    public void testLoadFromInvalidData() throws Exception {
        // Create an invalid task file to load from
        TestHelper.createInvalidTaskFile(testDirectoryString);

        // Command under test (load from the invalid task file)
        FilePathPair filesReplaced = taskDbInstance.load();

        // Assert the files have been replaced
        assertNotNull(filesReplaced);
    }

    @Test
    public void testTaskIdsResetUponLoad() throws Exception {
        // Create task 1
        TaskAttributes taskAttributes = new TaskAttributes();
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_1);
        taskAttributes.save();

        // Create task 2
        taskAttributes.setId(null);
        taskAttributes.setDescription(Constants.TEST_TASK_DESCRIPTION_2);
        taskAttributes.save();

        // Delete the first task
        taskDbInstance.destroy(1);

        // Command under test
        taskDbInstance.load();

        // There should only be one task with ID of 1
        assertTrue(taskDbInstance.hasId(1));
        assertFalse(taskDbInstance.hasId(2));
    }

}
