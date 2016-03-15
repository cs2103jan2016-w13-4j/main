package jfdi.logic.events;

/**
 * @author Liu Xinan
 */
public class UnaliasFailEvent {

    public enum Error {
        NON_EXISTENT_ALIAS, UNKNOWN
    }

    private String alias;
    private Error error;

    public UnaliasFailEvent(String alias, Error error) {
        this.alias = alias;
        this.error = error;
    }

    public String getAlias() {
        return alias;
    }

    public Error getError() {
        return error;
    }

}
