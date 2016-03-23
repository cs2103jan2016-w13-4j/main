//@@author A0121621Y
package jfdi.storage.exceptions;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class InvalidTaskParametersException extends Exception {

    private ArrayList<String> errorList;

    public InvalidTaskParametersException(ArrayList<String> errors) {
        errorList = errors;
    }

    /**
     * @return the ArrayList of error messages
     */
    public ArrayList<String> getErrorList() {
        return errorList;
    }

}
