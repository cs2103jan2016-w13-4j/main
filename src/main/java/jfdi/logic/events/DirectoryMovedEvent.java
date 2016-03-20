package jfdi.logic.events;

/**
 * @author Xinan
 */
public class DirectoryMovedEvent {

    private String newDirectory;

    public DirectoryMovedEvent(String newDirectory) {
        this.newDirectory = newDirectory;
    }

    public String getNewDirectory() {
        return newDirectory;
    }

}
