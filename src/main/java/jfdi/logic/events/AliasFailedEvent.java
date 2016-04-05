package jfdi.logic.events;

/**
 * @@author Liu Xinan
 */
public class AliasFailedEvent {

    public enum Error {
        INVALID_PARAMETERS, DUPLICATED_ALIAS, UNKNOWN
    }

    private String command;
    private String alias;
    private Error error;

    public AliasFailedEvent(String command, String alias, Error error) {
        this.command = command;
        this.alias = alias;
        this.error = error;
    }

    public String getCommand() {
        return command;
    }

    public String getAlias() {
        return alias;
    }

    public Error getError() {
        return error;
    }

}
