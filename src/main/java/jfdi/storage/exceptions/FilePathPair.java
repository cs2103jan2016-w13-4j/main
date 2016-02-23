package jfdi.storage.exceptions;

/**
 * FilePathPair pairs the original filepath of a file with the new filepath of a
 * file after a file has been moved.
 *
 * @author Thng Kai Yuan
 *
 */
public class FilePathPair {

    // The old and new filepaths
    private String oldFilePath = null;
    private String newFilePath = null;

    public FilePathPair(String oldFilePath, String newFilePath) {
        this.oldFilePath = oldFilePath;
        this.newFilePath = newFilePath;
    }

    /**
     * @return the oldFilePath
     */
    public String getOldFilePath() {
        return oldFilePath;
    }

    /**
     * @return the newFilePath
     */
    public String getNewFilePath() {
        return newFilePath;
    }
}
