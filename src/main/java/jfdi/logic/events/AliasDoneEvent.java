// @@author A0130195M
package jfdi.logic.events;

/**
 * @author Liu Xinan
 */
public class AliasDoneEvent {

    private String command;
    private String alias;

    public AliasDoneEvent(String command, String alias) {
        this.command = command;
        this.alias = alias;
    }

    public String getCommand() {
        return command;
    }

    public String getAlias() {
        return alias;
    }

}
