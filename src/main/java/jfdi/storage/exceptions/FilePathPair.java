//@@author A0121621Y
package jfdi.storage.exceptions;

/**
 * FilePathPair maps the original filepath of a file to the new filepath of the
 * file after it has been moved.
 *
 * @author Thng Kai Yuan
 *
 */
public class FilePathPair {

    private String oldFilePath = null;
    private String newFilePath = null;

    public FilePathPair(String oldFilePath, String newFilePath) {
        this.oldFilePath = oldFilePath;
        this.newFilePath = newFilePath;
    }

    public String getOldFilePath() {
        return oldFilePath;
    }

    public String getNewFilePath() {
        return newFilePath;
    }
}
