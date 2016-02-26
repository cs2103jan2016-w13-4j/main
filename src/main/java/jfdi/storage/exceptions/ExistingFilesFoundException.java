package jfdi.storage.exceptions;

import java.util.ArrayList;

/**
 * ExistingFilesFoundException is thrown when files are replaced with backups
 * made. The exception would contain an ArrayList of FilePathPairs that map
 * the replaced files to the location of their backups.
 *
 * @author Thng Kai Yuan
 */
@SuppressWarnings("serial")
public class ExistingFilesFoundException extends Exception {

    private ArrayList<FilePathPair> replacedFilePairs = null;

    public ExistingFilesFoundException(ArrayList<FilePathPair> replacedFilePairs) {
        this.replacedFilePairs = replacedFilePairs;
    }

    public ArrayList<FilePathPair> getReplacedFilePairs() {
        return replacedFilePairs;
    }

}
