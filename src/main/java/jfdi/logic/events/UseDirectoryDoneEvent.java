package jfdi.logic.events;

/**
 * @author Xinan
 */
public class UseDirectoryDoneEvent {

    private String newDirectory;

    public UseDirectoryDoneEvent(String newDirectory) {
        this.newDirectory = newDirectory;
    }

    public String getNewDirectory() {
        return newDirectory;
    }

}
