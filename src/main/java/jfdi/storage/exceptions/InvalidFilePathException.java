//@@author A0121621Y

package jfdi.storage.exceptions;

@SuppressWarnings("serial")
public class InvalidFilePathException extends Exception {

    private String path = null;

    public InvalidFilePathException(String path, String message) {
        super(message);
        this.path = path;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

}
