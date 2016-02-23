package jfdi.storage.exceptions;

/**
 * InvalidPathException is thrown when the program is unable to operate using
 * the given directory as its root directory for data storage.
 *
 * @author Thng Kai Yuan
 *
 */
@SuppressWarnings("serial")
public class InvalidPathException extends Exception {

    private String invalidDirectory = null;

    public InvalidPathException(String path) {
        invalidDirectory = path;
    }

    /**
     * @return the invalidDirectory
     */
    public String getInvalidDirectory() {
        return invalidDirectory;
    }

}
