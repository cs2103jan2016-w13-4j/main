package jfdi.test.storage;

import static org.junit.Assert.*;
import jfdi.storage.exceptions.InvalidFilePathException;

import org.junit.Test;

public class InvalidFilePathExceptionTest {

    @Test
    public void testInvalidFilePathException() {
        String path = "some path, doesn't quite matter what goes in here";
        String msg = "some message, doesn't quite matter as well";
        InvalidFilePathException exception = new InvalidFilePathException(path, msg);

        assertEquals(path, exception.getPath());
        assertEquals(msg, exception.getMessage());
    }

}
