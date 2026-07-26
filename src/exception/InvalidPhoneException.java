package exception;

public class InvalidPhoneException extends Exception {
    private static final long serialVersionUID = 1L;
    public InvalidPhoneException(String message) { super(message); }
}
