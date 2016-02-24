package parser;

import command.Command;

public class AddCommandBuilder extends CommandBuilder {

    public AddCommandBuilder(String input) {
        super(input);
    }

    @Override
    public Command build() {
        return new Command(ActionType.ADD, userInput, null, null, null, null);
    }

}
