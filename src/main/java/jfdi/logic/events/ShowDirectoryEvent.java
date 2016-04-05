package jfdi.logic.events;

/**
 * @author Liu Xinan
 */
public class ShowDirectoryEvent {

    private String pwd;

    public ShowDirectoryEvent(String pwd) {
        this.pwd = pwd;
    }

    public String getPwd() {
        return pwd;
    }

}
