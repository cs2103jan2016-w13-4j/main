package parser;

import command.Command;

public class EditCommandBuilder extends CommandBuilder {

    public EditCommandBuilder(String input) {
        super(input);
    }

    @Override
    public Command build() {
        return new Command(ActionType.EDIT, userInput, null, null, null, null);
    }

}
