package com.bearcode.ovf.model.common;

/**
 * Created by leonid on 03.11.15.
 */
public enum PhoneNumberType {
    Home,
    Work,
    Mobile,
    Other;

    public static String[] getStringValues() {
        return new String[] { Home.name(), Work.name(), Mobile.name(), Other.name() };
    }
}
