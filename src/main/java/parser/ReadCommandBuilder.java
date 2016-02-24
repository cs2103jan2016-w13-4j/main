package parser;

import command.Command;

public class ReadCommandBuilder extends CommandBuilder {

    public ReadCommandBuilder(String input) {
        super(input);
    }

    @Override
    public Command build() {
        return new Command(ActionType.READ, userInput, null, null, null, null);
    }

}
