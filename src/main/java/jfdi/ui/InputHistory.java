//@@author A0121621Y

package jfdi.ui;

import java.util.Stack;

public class InputHistory {

    private Stack<String> previousStack = null;
    private Stack<String> nextStack = null;
    private String floatingString = null;

    InputHistory() {
        previousStack = new Stack<String>();
        nextStack = new Stack<String>();
    }

    /**
     * @return the previous string from the input history
     */
    public String getPrevious() {
        if (previousStack.isEmpty()) {
            return null;
        }

        String previousString = previousStack.pop();
        addToNext(previousString);
        return previousString;
    }

    /**
     * @return the next string from the input history
     */
    public String getNext() {
        if (nextStack.isEmpty()) {
            return null;
        }

        String nextString = nextStack.pop();
        addToPrevious(nextString);
        return nextString;
    }
    
    /**
     * Clears the input history so that undo would not work
     */
    public void clearHistory() {
        previousStack.clear();
        nextStack.clear();
    }

    /**
     * Adds a new input to the input history.
     *
     * @param input
     *            the new input
     */
    public void addInput(String input) {
        if (floatingString != null) {
            previousStack.push(floatingString);
            floatingString = null;
        }

        while (!nextStack.isEmpty()) {
            previousStack.push(nextStack.pop());
        }

        if (previousStack.empty() || !previousStack.peek().equals(input)) {
            previousStack.push(input);
        }
    }

    private void addToNext(String input) {
        if (floatingString != null) {
            nextStack.push(floatingString);
        }
        floatingString = input;
    }

    private void addToPrevious(String input) {
        if (floatingString != null) {
            previousStack.push(floatingString);
        }
        floatingString = input;
    }

}
