package jfdi.logic.events;

import jfdi.storage.exceptions.FilePathPair;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Xinan
 */
public class FilesReplacedEvent {

    private String newDirectory = null;
    private ArrayList<FilePathPair> filePathPairs;

    public FilesReplacedEvent(ArrayList<FilePathPair> filePathPairs) {
        this.filePathPairs = filePathPairs;
    }

    public FilesReplacedEvent(String newDirectory, ArrayList<FilePathPair> filePathPairs) {
        this.newDirectory = newDirectory;
        this.filePathPairs = filePathPairs;
    }

    public ArrayList<FilePathPair> getFilePathPairs() {
        return filePathPairs;
    }

    public String getNewDirectory() {
        return newDirectory;
    }

}
