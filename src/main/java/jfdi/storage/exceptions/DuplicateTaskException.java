//@@author A0121621Y
package jfdi.storage.exceptions;

import jfdi.storage.apis.TaskAttributes;

@SuppressWarnings("serial")
public class DuplicateTaskException extends Exception {

    private TaskAttributes duplicateTaskAttributes = null;

    public DuplicateTaskException(TaskAttributes taskAttributes) {
        duplicateTaskAttributes = taskAttributes;
    }

    /**
     * @return the duplicate task attributes
     */
    public TaskAttributes getDuplicateTaskAttributes() {
        return duplicateTaskAttributes;
    }

}
