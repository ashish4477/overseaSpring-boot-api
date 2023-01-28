package com.bearcode.ovf.webservices.grecaptcha.model;

/**
 * @author leonid.
 */
public class ReCaptchaResponse {

    private boolean success;
    private String challenge_ts;                   // timestamp of the challenge load (ISO format yyyy-MM-dd'T'HH:mm:ssZZ)
    private String hostname;                       // the hostname of the site where the reCAPTCHA was solved
    private String[] errorCodes = new String[]{}; // optional

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess( boolean success ) {
        this.success = success;
    }

    public String getChallenge_ts() {
        return challenge_ts;
    }

    public void setChallenge_ts( String challenge_ts ) {
        this.challenge_ts = challenge_ts;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname( String hostname ) {
        this.hostname = hostname;
    }

    public String[] getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes( String[] errorCodes ) {
        this.errorCodes = errorCodes;
    }
}
