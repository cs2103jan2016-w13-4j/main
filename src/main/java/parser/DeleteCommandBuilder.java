package parser;

import command.Command;

public class DeleteCommandBuilder extends CommandBuilder {

    public DeleteCommandBuilder(String input) {
        super(input);
    }

    @Override
    public Command build() {
        return new Command(ActionType.DELETE, userInput, null, null, null, null);
    }

}
