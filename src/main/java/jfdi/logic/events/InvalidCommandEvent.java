package jfdi.logic.events;

/**
 * @author Liu Xinan
 */
public class InvalidCommandEvent {

    private String inputString;

    public InvalidCommandEvent(String inputString) {
        this.inputString = inputString;
    }

    public String getInputString() {
        return inputString;
    }

}
