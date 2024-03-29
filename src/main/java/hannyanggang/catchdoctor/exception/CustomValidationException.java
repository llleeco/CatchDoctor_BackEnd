package hannyanggang.catchdoctor.exception;

public class CustomValidationException extends RuntimeException {
    private final int status;

    public CustomValidationException(int status, String message) {
        super(message);
        //this.httpStatus = httpStatus;
        this.status=status;
    }


    public int getStatus() {
        return status;
    }
}
