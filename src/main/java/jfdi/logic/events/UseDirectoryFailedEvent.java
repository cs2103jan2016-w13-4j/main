package jfdi.logic.events;

import jfdi.storage.exceptions.FilePathPair;

import java.util.ArrayList;

/**
 * @author Xinan
 */
public class UseDirectoryFailedEvent {

    public enum Error {
        FILE_REPLACED, INVALID_PATH, UNKNOWN
    }

    private String newDirectory;
    private Error error;
    private ArrayList<FilePathPair> filePathPairs;

    public UseDirectoryFailedEvent(String newDirectory, Error error, ArrayList<FilePathPair> filePathPairs) {
        this.newDirectory = newDirectory;
        this.error = error;
        this.filePathPairs = filePathPairs;
    }

    public UseDirectoryFailedEvent(String newDirectory, Error error) {
        this.newDirectory = newDirectory;
        this.error = error;
        this.filePathPairs = null;
    }

    public String getNewDirectory() {
        return newDirectory;
    }

    public Error getError() {
        return error;
    }

    public ArrayList<FilePathPair> getFilePathPairs() {
        return filePathPairs;
    }

}
