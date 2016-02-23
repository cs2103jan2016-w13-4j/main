package jfdi.storage.exceptions;

import java.util.ArrayList;

/**
 * ExistingFilesFoundException is thrown when files are replaced with backups
 * made.
 *
 * @author Thng Kai Yuan
 */
@SuppressWarnings("serial")
public class ExistingFilesFoundException extends Exception {

    // An ArrayList of FilePathPairs pairing each original filepath to their
    // backup filepath
    private ArrayList<FilePathPair> replacedFilePairs = null;

    public ExistingFilesFoundException(ArrayList<FilePathPair> replacedFilePairs) {
        this.replacedFilePairs = replacedFilePairs;
    }

    /**
     * @return the ArrayList of replacedFilePairs
     */
    public ArrayList<FilePathPair> getReplacedFilePairs() {
        return replacedFilePairs;
    }

}
