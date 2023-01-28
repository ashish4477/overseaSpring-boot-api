package com.bearcode.ovf.tools;

/**
 * Created by leonid on 16.04.15.
 */
public class GetResponseException extends Exception {

    public GetResponseException() {
    }

    public GetResponseException( String message ) {
        super( message );
    }

    public GetResponseException( String message, Throwable cause ) {
        super( message, cause );
    }

    public GetResponseException( Throwable cause ) {
        super( cause );
    }
}
