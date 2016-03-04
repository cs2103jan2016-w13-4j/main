package dummy;

import jfdi.logic.commands.ExitCommand;
import jfdi.logic.commands.InvalidCommand;
import jfdi.logic.commands.ListCommand;
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
                ListCommand.Builder builder = new ListCommand.Builder();
                for (int i = 1; i < parts.length; i++) {
                    builder.addTag(parts[i]);
                }
                return builder.build();
            case "exit":
                return new ExitCommand.Builder().build();
            default:
                return new InvalidCommand.Builder().build();
        }
    }
}
