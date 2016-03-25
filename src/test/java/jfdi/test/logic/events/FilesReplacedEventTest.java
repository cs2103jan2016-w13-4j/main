package jfdi.test.logic.events;

import jfdi.logic.events.FilesReplacedEvent;
import jfdi.storage.exceptions.FilePathPair;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Xinan
 */
public class FilesReplacedEventTest {

    @Mock
    private ArrayList<FilePathPair> filePathPairs;

    private String newDirectory = ".";

    @Test
    public void getFilePathPairs() throws Exception {
        FilesReplacedEvent event1 = new FilesReplacedEvent(filePathPairs);
        assertEquals(filePathPairs, event1.getFilePathPairs());

        FilesReplacedEvent event2 = new FilesReplacedEvent(newDirectory, filePathPairs);
        assertEquals(filePathPairs, event2.getFilePathPairs());
    }

    @Test
    public void getNewDirectory() throws Exception {
        FilesReplacedEvent event1 = new FilesReplacedEvent(filePathPairs);
        assertNull(event1.getNewDirectory());

        FilesReplacedEvent event2 = new FilesReplacedEvent(newDirectory, filePathPairs);
        assertEquals(newDirectory, event2.getNewDirectory());
    }

}
