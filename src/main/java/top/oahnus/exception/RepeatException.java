package top.oahnus.exception;

/**
 * Created by oahnus on 2016/11/24.
 */
public class RepeatException extends SeckillException {
    public RepeatException(String message) {
        super(message);
    }

    public RepeatException(String message, Throwable cause) {
        super(message, cause);
    }
}
