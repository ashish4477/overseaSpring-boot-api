package com.bearcode.ovf.webservices.eod.model;

import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.eod.AdditionalAddressType;
import com.bearcode.ovf.model.eod.Officer;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author leonid.
 */
public class EodAddress extends Address implements Serializable, Comparable<EodAddress> {

    private static final long serialVersionUID = 6837761067515136271L;
    private String website;
    private List<EodAddressFunction> functions;
    private boolean isPhysical = false;
    private boolean isRegularMail = false;
    private String primaryContactUri;
    private AdditionalAddressType type = new AdditionalAddressType();
    private Officer mainOfficer;
    private String mainEmail;
    private String mainPhoneNumber;
    private String mainFaxNumber;

    public String getWebsite() {
        return website;
    }

    public void setWebsite( String website ) {
        this.website = website;
    }

    public List<EodAddressFunction> getFunctions() {
        return functions;
    }

    public void setFunctions( List<EodAddressFunction> functions ) {
        this.functions = functions;
    }

    public boolean isPhysical() {
        return isPhysical;
    }

    public void setPhysical( boolean isPhysical ) {
        this.isPhysical = isPhysical;
    }

    public void setIsPhysical( boolean isPhysical ) {
        this.isPhysical = isPhysical;
    }
    public boolean isRegularMail() {
        return isRegularMail;
    }

    public void setRegularMail( boolean isRegularMail ) {
        this.isRegularMail = isRegularMail;
    }

    public void setIsRegularMail( boolean isRegularMail ) {
        this.isRegularMail = isRegularMail;
    }

    public String getPrimaryContactUri() {
        return primaryContactUri;
    }

    public void setPrimaryContactUri( String primaryContactUri ) {
        this.primaryContactUri = primaryContactUri;
    }

    public AdditionalAddressType getType() {
        return type;
    }

    public Address getAddress() {
        return this;
    }

    public String getEmail() {
        return mainEmail;
    }

    public Officer getMainOfficer() {
        return mainOfficer;
    }

    public void setMainOfficer( Officer mainOfficer ) {
        this.mainOfficer = mainOfficer;
    }

    public String getMainEmail() {
        return mainEmail;
    }

    public void setMainEmail( String mainEmail ) {
        this.mainEmail = mainEmail;
    }

    public String getMainPhoneNumber() {
        return mainPhoneNumber;
    }

    public void setMainPhoneNumber( String mainPhoneNumber ) {
        this.mainPhoneNumber = mainPhoneNumber;
    }

    public String getMainFaxNumber() {
        return mainFaxNumber;
    }

    public void setMainFaxNumber( String mainFaxNumber ) {
        this.mainFaxNumber = mainFaxNumber;
    }

    public boolean hasFunction( EodAddressFunction function ) {
        if ( functions != null ) {
            for ( EodAddressFunction hasFunction : functions ) {
                if ( hasFunction == function ) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCombinedLabel() {
        return functions != null && functions.size() != EodAddressFunction.values().length;
    }

    public String getShortLabel() {
        String label = "";
        if ( functions != null ) {
            if ( functions.size() == 1 ) {
                label = functions.get( 0 ).getDescription();
            }
            if ( functions.size() > 1 && functions.size() < EodAddressFunction.values().length ) {
                boolean removeDomestic = hasFunction( EodAddressFunction.OVS_REQ ) && hasFunction( EodAddressFunction.DOM_REQ ) ||
                        hasFunction( EodAddressFunction.DOM_RET ) && hasFunction( EodAddressFunction.OVS_RET );
                boolean addReturn = hasFunction( EodAddressFunction.DOM_RET ) && hasFunction( EodAddressFunction.DOM_REQ ) ||
                        hasFunction( EodAddressFunction.OVS_RET ) && hasFunction( EodAddressFunction.OVS_REQ);
                label = functions.get( 0 ).getDescription();
                if ( removeDomestic ) {
                    label = label.replaceAll( "(Domestic|Overseas) ", "" );
                }
                if ( addReturn ) {
                    label = label.replaceAll( "Request|Return", "Request/Return" );
                }
            }
        }
        return label;
    }

    public boolean isShowOnOVF() {
        return !(functions != null && functions.size() == 1 && hasFunction( EodAddressFunction.DOM_VR ));
    }

    private int sortNumber() {
        if ( functions != null ) {
            if ( hasFunction( EodAddressFunction.DOM_VR ) ) return 0;
            if ( hasFunction( EodAddressFunction.OVS_REQ ) || hasFunction( EodAddressFunction.DOM_REQ ) ) return 2;
            if ( hasFunction( EodAddressFunction.OVS_RET ) || hasFunction( EodAddressFunction.DOM_RET ) ) return 4;
        }
        return 100;
    }

    @Override
    public int compareTo( EodAddress o ) {
        return this.sortNumber() - o.sortNumber();
    }
}
