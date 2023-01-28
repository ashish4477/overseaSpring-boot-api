package com.bearcode.commons.util;

/**
 * @author leonid.
 */
public class AmbiguousParameterException extends RuntimeException {
    private static final long serialVersionUID = -4602937654976506352L;

    public AmbiguousParameterException() {
    }

    public AmbiguousParameterException(String message) {
        super(message);
    }

    public AmbiguousParameterException(Throwable cause) {
        super(cause);
    }

    public AmbiguousParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
