package com.bearcode.ovf.model.questionnaire;

import com.bearcode.ovf.model.common.DependentRoot;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;

public enum FlowType implements Serializable, DependentRoot {

    RAVA("overseas-military-absentee-ballot", "rava"),
    FWAB("fwab", "fwab"),
    DOMESTIC_REGISTRATION("voter-registration", "domestic_registration"),
    DOMESTIC_ABSENTEE("domestic-absentee-ballot", "domestic_absentee");

    private final String flow;
    private final String pageSlug;

    FlowType(String flow, String pageSlug) {
        this.flow = flow;
        this.pageSlug = pageSlug;
    }

    public static FlowType getFormSlugByFlow(String inputFlow) {
        return Arrays.stream(values())
                .filter(value -> StringUtils.equalsIgnoreCase(value.getFlow(), inputFlow)).findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public PageType getPageType() {
        switch ( this ) {
            case RAVA:
            case FWAB:
                return PageType.OVERSEAS;

            case DOMESTIC_REGISTRATION:
                return PageType.DOMESTIC_REGISTRATION;

            case DOMESTIC_ABSENTEE:
                return PageType.DOMESTIC_ABSENTEE;

            default:
                throw new IllegalArgumentException( "Page type not found for FlowType=" + this.name() );
        }
    }

    public static boolean isValidPageSlug(String inputPageSlug) {
        return Arrays.stream(values()).map(FlowType::getPageSlug)
                .anyMatch(type -> StringUtils.equalsIgnoreCase(type, inputPageSlug));
    }

    public static boolean isValidFlow(String inputFlow) {
        return Arrays.stream(values()).map(FlowType::getFlow)
                .anyMatch(type -> StringUtils.equalsIgnoreCase(type, inputFlow));
    }

    public String getFileNameParam() {
        switch (this) {
            case DOMESTIC_REGISTRATION:
                return "Voter-Registration";
            case DOMESTIC_ABSENTEE:
                return "Absentee-Ballot-Request";
            case RAVA:
                return "Voter-Registration_Absentee-Ballot-Request(FPCA)";
            case FWAB:
                return "Federal-Write-In-Absentee-Ballot(FWAB)";
            default:
                throw new IllegalArgumentException( "Incorrect FlowType=" + this.name() );
        }
    }

    public String getFlow() {
        return flow;
    }

    public String getPageSlug() {
        return pageSlug;
    }

    @Override
    public String getName() {
        return name();
    }
}
