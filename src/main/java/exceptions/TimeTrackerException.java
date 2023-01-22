package exceptions;

/**
 * This class is just an extension of the {@link RuntimeException RuntimeException}.
 * @author Patrich Tivoli
 */
public class TimeTrackerException extends RuntimeException{

    public TimeTrackerException() {
        super();
    }

    public TimeTrackerException(String message) {
        super(message);
    }

    public TimeTrackerException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeTrackerException(Throwable cause) {
        super(cause);
    }

    protected TimeTrackerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
