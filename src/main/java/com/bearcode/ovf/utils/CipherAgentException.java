package com.bearcode.ovf.utils;

/**
 * @author leonid.
 */
public class CipherAgentException extends Exception {
    public CipherAgentException() {
    }

    public CipherAgentException( String message, Throwable cause ) {
        super( message, cause );
    }

    public CipherAgentException( Throwable cause ) {
        super( cause );
    }

    public CipherAgentException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }

    public CipherAgentException( String message ) {
        super( message );
    }
}
