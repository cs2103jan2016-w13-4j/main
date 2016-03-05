package jfdi.storage.exceptions;

@SuppressWarnings("serial")
public class InvalidIdException extends Exception {

    private Integer invalidId = null;

    public InvalidIdException(Integer id) {
        this.invalidId = id;
    }

    public Integer getInvalidId() {
        return invalidId;
    }

}
