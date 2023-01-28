package com.bearcode.ovf.actions.eod;

/**
 * @author leonid.
 */
public interface ICaptchaUse {

    public static final String USE_COUNT = "use_captcha_count";
    public static final String SHOW_CAPTHCA = "showCaptcha";
    public static final String CAPTHCA_PARAM = "g-recaptcha-response"; // "captchaStr";
    public static final String SITE_KEY_ATTRIBUTE = "greSiteKey";
    public static final String SITE_KEY_ATTRIBUTE_SKIMM = "greSiteKeySkimm";
    public static final String SITE_KEY_ATTRIBUTE_VOTE411 = "greSiteKeyVote411";

    /**
     * Number of requests for showing LEO info after entering CAPTCHA info and
     * before asking CAPTCHA again.
     */
    public static final int number4Captcha = 9;

    public String getSiteKey();

    public String getSiteKeySkim();

    public String getSiteKeyVote411();

}
