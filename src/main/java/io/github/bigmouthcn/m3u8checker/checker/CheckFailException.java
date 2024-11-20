package io.github.bigmouthcn.m3u8checker.checker;

/**
 * @author Allen Hu
 * @date 2024/11/15
 */
public class CheckFailException extends Exception {

    private final FailType failType;

    public CheckFailException(FailType failType) {
        this.failType = failType;
    }

    public CheckFailException(String message, FailType failType) {
        super(message);
        this.failType = failType;
    }

    public CheckFailException(String message, Throwable cause, FailType failType) {
        super(message, cause);
        this.failType = failType;
    }

    public CheckFailException(Throwable cause, FailType failType) {
        super(cause);
        this.failType = failType;
    }

    public CheckFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, FailType failType) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.failType = failType;
    }

    public FailType getFailType() {
        return failType;
    }
}
