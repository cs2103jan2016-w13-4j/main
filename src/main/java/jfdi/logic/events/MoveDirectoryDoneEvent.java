package jfdi.logic.events;

/**
 * @author Liu Xinan
 */
public class MoveDirectoryDoneEvent {

    private String newDirectory;

    public MoveDirectoryDoneEvent(String newDirectory) {
        this.newDirectory = newDirectory;
    }

    public String getNewDirectory() {
        return newDirectory;
    }

}
