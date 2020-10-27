package com.lyf.common.exception;

/**
 * @author lyf
 * @date 2020/6/23-4:07
 */
public class UserNameException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserNameException() {
        super();
    }

    public UserNameException(String message, Throwable cause, boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UserNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNameException(String message) {
        super(message);
    }

    public UserNameException(Throwable cause) {
        super(cause);
    }

}
