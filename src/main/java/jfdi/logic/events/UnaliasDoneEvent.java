package jfdi.logic.events;

/**
 * @author Liu Xinan
 */
public class UnaliasDoneEvent {

    private String alias;

    public UnaliasDoneEvent(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

}
