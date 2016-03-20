package jfdi.logic.events;

import jfdi.storage.exceptions.FilePathPair;

import java.util.ArrayList;

/**
 * @author Xinan
 */
public class MoveDirectoryFailedEvent {


    public enum Error {
        FILE_REPLACED, ACCESS_DENIED, UNKNOWN
    }

    private String newDirectory;
    private Error error;
    private ArrayList<FilePathPair> filePathPairs;

    public MoveDirectoryFailedEvent(String newDirectory, Error error, ArrayList<FilePathPair> filePathPairs) {
        this.newDirectory = newDirectory;
        this.error = error;
        this.filePathPairs = filePathPairs;
    }

    public MoveDirectoryFailedEvent(String newDirectory, Error error) {
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
