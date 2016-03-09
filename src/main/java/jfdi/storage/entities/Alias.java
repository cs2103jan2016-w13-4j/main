package jfdi.storage.entities;

public class Alias {

    private String alias;
    private String command;

    public Alias(String alias, String command) {
        this.alias = alias;
        this.command = command;
    }

    public String getAlias() {
        return alias;
    }

    public String getCommand() {
        return command;
    }
}
