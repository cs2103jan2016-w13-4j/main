package dummy;

import jfdi.logic.commands.*;
import jfdi.logic.interfaces.Command;

/**
 * @author Liu Xinan
 */
public class DummyParser {

    private static DummyParser ourInstance = new DummyParser();

    private DummyParser() {
    }

    public static DummyParser getInstance() {
        return ourInstance;
    }

    public Command parse(String input) {
        String[] parts = input.trim().split("\\s+");
        if (parts[0].isEmpty()) {
            return new InvalidCommand.Builder().build();
        }

        switch (parts[0]) {
            case "list":
                ListCommand.Builder listCommandBuilder = new ListCommand.Builder();
                for (int i = 1; i < parts.length; i++) {
                    listCommandBuilder.addTag(parts[i]);
                }
                return listCommandBuilder.build();
            case "exit":
                return new ExitCommand.Builder().build();
            case "add":
                return new AddTaskCommand.Builder().setDescription(parts[1]).build();
            case "delete":
                DeleteTaskCommand.Builder deleteCommandBuilder =  new DeleteTaskCommand.Builder();
                for (int i = 1; i < parts.length; i++) {
                    deleteCommandBuilder.addId(Integer.parseInt(parts[i]));
                }
                return deleteCommandBuilder.build();
            default:
                return new InvalidCommand.Builder().setInputString(input).build();
        }
    }
}
