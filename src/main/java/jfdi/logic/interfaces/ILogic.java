package jfdi.logic.interfaces;

/**
 * @author Liu Xinan
 */
public interface ILogic {

    /**
     * Called by UI to handle user input.
     * It should convert the input to a specific command object and execute it.
     * It should handle erroneous input as well.
     *
     * @param input Input from user
     */
    void handleInput(String input);
}
