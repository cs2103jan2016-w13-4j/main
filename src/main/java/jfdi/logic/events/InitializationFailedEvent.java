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

    public InitializationFailedEvent(Error error) {
        this.error = error;
    }

    public InitializationFailedEvent(Error error, ArrayList<FilePathPair> filePathPairs) {
        this.error = error;
        this.filePathPairs = filePathPairs;
    }

    public Error getError() {
        return error;
    }

    public ArrayList<FilePathPair> getFilePathPairs() {
        return filePathPairs;
    }

}
