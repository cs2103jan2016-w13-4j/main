package jfdi.logic.events;

import jfdi.storage.exceptions.FilePathPair;

import java.util.ArrayList;

/**
 * @author Xinan
 */
public class InitializationFailedEvent {

    public enum Error {
        FILE_REPLACED, INVALID_PATH, UNKNOWN
    }

    private Error error;
    private ArrayList<FilePathPair> filePathPairs;
    private String path;

    public InitializationFailedEvent(Error error, String path) {
        this.error = error;
        this.path = path;
        this.filePathPairs = null;
    }

    public InitializationFailedEvent(Error error, ArrayList<FilePathPair> filePathPairs) {
        this.error = error;
        this.filePathPairs = filePathPairs;
        this.path = null;
    }

    public Error getError() {
        return error;
    }

    public ArrayList<FilePathPair> getFilePathPairs() {
        return filePathPairs;
    }

    public String getPath() {
        return path;
    }

}
