package exception;

public class DuplicatePhoneException extends Exception {
    private static final long serialVersionUID = 1L;
    public DuplicatePhoneException(String message) { super(message); }
}
