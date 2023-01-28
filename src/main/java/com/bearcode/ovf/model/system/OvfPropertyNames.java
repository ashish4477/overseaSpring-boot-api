package com.bearcode.ovf.model.system;

/**
 * @author leonid.
 */
public enum OvfPropertyNames {
    //FACT_CHECK_SERVICE_AUTH_PARAM_NAME ("FactCheck Service", "Authentication param", ""),
    //FACT_CHECK_SERVICE_AUTH_KEY("FactCheck Service", "Authentication key", ""),
    LOCAL_ELECTION_SERVICE_URL("Local Election", "URL of the API", System.getenv("LOCAL_ELECTION_URL")),
    LOCAL_ELECTION_AUTH_TOKEN("Local Election", "Authentication header", "Token " + System.getenv("LOCAL_ELECTION_AUTH_TOKEN")),
    EOD_API_SERVICE_URL("EOD API", "URL of the API", System.getenv("EOD_API_SERVICE_URL")),
    EOD_API_AUTH_TOKEN("EOD API", "Authentication header", "OAuth " + System.getenv("LOCAL_ELECTION_AUTH_TOKEN")),
    EOD_API_CORRECTION_URL_TEMPLATE("EOD API", "Corrections URL template", "http://electionmanagerstage-demo2.us-east-1.elasticbeanstalk.com/corrections/legacy/LEO_ID/submit/"),
    EOD_API_CORRECTION_URL_PARAM("EOD API", "Corrections URL param", "LEO_ID"),
    RE_CAPTCHA_SITE_KEY("Google reCaptcha", "Site key", System.getenv("RECAPTCHA_SITE_KEY")),
    RE_CAPTCHA_SECRET_KEY("Google reCaptcha", "Secret key", System.getenv("RECAPTCHA_SECRET_KEY")),
    RE_CAPTCHA_SERVICE_URL("Google reCaptcha", "Service URL", "https://www.google.com/recaptcha/api/siteverify"),
    RE_CAPTCHA_SITE_KEY_SKIMM("Google reCaptcha  skimm","Skimm Site key",System.getenv("RE_CAPTCHA_SITE_KEY_SKIMM")),
    RE_CAPTCHA_SECRET_KEY_SKIMM("Google reCaptcha skimm", "Secret key", System.getenv("RECAPTCHA_SECRET_KEY_SKIMM")),
    RE_CAPTCHA_SITE_KEY_VOTE411("Google reCaptcha  vote411","Vote411 Site key",System.getenv("RE_CAPTCHA_SITE_KEY_VOTE411")),
    RE_CAPTCHA_SECRET_KEY_VOTE411("Google reCaptcha vote411", "Secret key", System.getenv("RECAPTCHA_SECRET_KEY_VOTE411")),

    //VOTESMART_AUTH_KEY("Vote Smart", "Authentication key", "")
    EMAIL_SERVICE_TUMBLER_SWITCH("Email Service", "Enable tumbler (\"1\"/\"0\")", "1"),
    EMAIL_SERVICE_RATE_MAX_CALLS("Email Service", "Rate (a) max calls", "70"),
    EMAIL_SERVICE_RATE_PER_SECOND("Email Service", "Rate (b) per second", "1"),
    EMAIL_SERVICE_NUMBER_OF_RETRY_ATTEMPT("Email Service", "Max number of retry attempt", "10"),
    EMAIL_SERVICE_DAYS_TO_KEEP_LOG("Email Service", "Max days to keep email log", "30"),
    EMAIL_SERVICE_BATCH_SIZE("Email Service", "Max number of emails in a run", "1500"),
    EMAIL_SERVICE_MILLIS_FOR_RETRY_DELAY("Email Service", "Milliseconds for retry delay", "6000"),
    SEND_GRID_API_SERVICE_URL("SendGrid","URL of the API", "https://sendgrid.com/v3"),
    SEND_GRID_API_AUTH_TOKEN("SendGrid","API key", System.getenv("SENDGRID_API_KEY")),
    SEND_GRID_SEND_UPDATES("SendGrid","Send new subscribes and updates", "1"),
    SEND_GRID_GET_UNSUBSCRIBES("SendGrid","Get unsubscribes", "1"),
    SEND_GRID_LOG_LEVEL("SendGrid","Logging level", "INFO"),
    SEND_GRID_LOG_AVAILABLE("SendGrid","Logging available", "1"),
    S3_ACCESS_KEY("S3","aws access key", System.getenv("S3_ACCESS_KEY")),
    S3_SECRET_KEY("S3","aws secret key", System.getenv("S3_SECRET_KEY")),
    IS_JOB_ENABLED("Job","Is job scheduler enabled",System.getenv("IS_JOB_ENABLED")),
    GRANT_TOKEN("Zoho", "GRANT_TOKEN", "1000.97f8957633fa21c1babd97a5abc3bbf0.4345cd01ca96d77fcc64d5bc0c03d40d");


    private String groupName;
    private String propertyName;
    private String defaultValue;

    OvfPropertyNames( String groupName, String propertyName, String defaultValue ) {
        this.groupName = groupName;
        this.propertyName = propertyName;
        this.defaultValue = defaultValue;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
