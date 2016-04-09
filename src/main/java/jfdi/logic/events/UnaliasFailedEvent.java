// @@author A0130195M

package jfdi.logic.events;

/**
 * @author Liu Xinan
 */
public class UnaliasFailedEvent {

    public enum Error {
        NON_EXISTENT_ALIAS,
        UNKNOWN
    }

    private String alias;
    private Error error;

    public UnaliasFailedEvent(String alias, Error error) {
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
