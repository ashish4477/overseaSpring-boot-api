package com.bearcode.ovf.model.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Type;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Nov 6, 2007
 * Time: 8:02:15 PM
 *
 * @author Leonid Ginzburg
 */

@Entity
@Table(name = "faces_config")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class FaceConfig implements DependentRoot, Serializable {
    private static final long serialVersionUID = 9104394599252381043L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url_path")
    private String urlPath = "";

    @Column(name = "path_prefix")
    private String relativePrefix = "";

    @Column(name = "default", nullable = false, columnDefinition = "tinyint(1) NOT NULL")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean defaultPath = false;

    @Column(name = "scytl_integration", nullable = false, columnDefinition = "tinyint(1) NOT NULL")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean scytlIntegration = false;

    @Column(name = "envelope", nullable = false, columnDefinition = "tinyint(1) NOT NULL")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean useEnvelope = false;

    @Column(name = "fax_page", nullable = false, columnDefinition = "tinyint(1) NOT NULL")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean useFaxPage = false;

    @Column(name = "notarization_page", nullable = false, columnDefinition = "tinyint(1) NOT NULL")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean useNotarizationPage = false;

    @Column(name = "blank_addendum_page", nullable = false, columnDefinition = "tinyint(1) NOT NULL")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean useBlankAddendumPage = false;

    @Column(name = "external_content_url")
    private String externalContentUrl = "";

    @Column(name = "user_validation_skip_fields")
    private String userValidationSkipFields = "";

    @Column( name = "drupal_url" )
    private String drupalUrl = "";

    @Column(name = "use_captcha", nullable = false, columnDefinition = "tinyint(1) NOT NULL")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean useCaptcha = true;

    @Column(name = "login_allowed", nullable = false, columnDefinition = "tinyint(1) NOT NULL")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean loginAllowed = true;

    /**
     * Create an account automatically for a new user just after entering voter information
     */
    @Column(name = "auto_create_account", nullable = false, columnDefinition = "tinyint(1) NOT NULL")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean autoCreateAccount = false;

    public String getUserValidationSkipFields() {
        return userValidationSkipFields;
    }

    public void setUserValidationSkipFields( String userValidationSkipFields ) {
        this.userValidationSkipFields = userValidationSkipFields;
    }

    public boolean isScytlIntegration() {
        return scytlIntegration;
    }

    public void setScytlIntegration( boolean scytlIntegration ) {
        this.scytlIntegration = scytlIntegration;
    }

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath( String urlPath ) {
        this.urlPath = urlPath;
    }

    public String getRelativePrefix() {
        return relativePrefix;
    }

    public void setRelativePrefix( String relativePrefix ) {
        this.relativePrefix = relativePrefix;
    }

    public boolean isDefaultPath() {
        return defaultPath;
    }

    public void setDefaultPath( boolean defaultPath ) {
        this.defaultPath = defaultPath;
    }

    public boolean isUseEnvelope() {
        return useEnvelope;
    }

    public void setUseEnvelope( boolean useEnvelope ) {
        this.useEnvelope = useEnvelope;
    }

    public boolean isUseFaxPage() {
        return useFaxPage;
    }

    public void setUseFaxPage( boolean useFaxPage ) {
        this.useFaxPage = useFaxPage;
    }

    public boolean isUseNotarizationPage() {
        return useNotarizationPage;
    }

    public void setUseNotarizationPage( boolean useNotarizationPage ) {
        this.useNotarizationPage = useNotarizationPage;
    }

    public boolean isUseBlankAddendumPage() {
        return useBlankAddendumPage;
    }

    public void setUseBlankAddendumPage( boolean useBlankAddendumPage ) {
        this.useBlankAddendumPage = useBlankAddendumPage;
    }

    public String getExternalContentUrl() {
        return externalContentUrl;
    }

    public void setExternalContentUrl( String externalContentUrl ) {
        this.externalContentUrl = externalContentUrl;
    }

    public String getDrupalUrl() {
        return drupalUrl;
    }

    public void setDrupalUrl( String drupalUrl ) {
        this.drupalUrl = drupalUrl;
    }

    public String getName() {
        return StringUtils.capitalize( relativePrefix.replaceAll( "faces/", "" ) );
    }

    public boolean isUseCaptcha() {
        return useCaptcha;
    }

    public void setUseCaptcha(boolean useCaptcha) {
        this.useCaptcha = useCaptcha;
    }

    public boolean isLoginAllowed() {
        return loginAllowed;
    }

    public void setLoginAllowed(boolean loginAllowed) {
        this.loginAllowed = loginAllowed;
    }

    public boolean isAutoCreateAccount() {
        return autoCreateAccount;
    }

    public void setAutoCreateAccount( boolean autoCreateAccount ) {
        this.autoCreateAccount = autoCreateAccount;
    }

    /**
     * Returns a map of method name and method parameter value
     * for setter methods on the PdfAnswers class
     * <p/>
     * TODO: store these in the db keyed to FaceConfig.id instead of getName()
     *
     * @return HashMap<String,String> where each key is a valid PdfAnswers.set* method and value is the parameter passed to the method.
     */
    public Map<String, String> getPresetPdfAnswersFields() {

        String name = getName();

        HashMap<String, String> presets = new HashMap<String, String>( 0 );

        if ( name.equalsIgnoreCase( "alabama" ) ) {
            presets.put( "getVotingAddress.setState", "AL" );
        }
        if ( name.equalsIgnoreCase( "minnesota" ) ) {
            presets.put( "getVotingAddress.setState", "MN" );
        }
        if ( name.equalsIgnoreCase( "ohio" ) ) {
            presets.put( "getVotingAddress.setState", "OH" );
        }
        if ( name.equalsIgnoreCase( "westvirginia" ) ) {
            presets.put( "getVotingAddress.setState", "WV" );
        }
        if ( name.equalsIgnoreCase( "kentucky" ) ) {
            presets.put( "getVotingAddress.setState", "KY" );
        }
        if ( name.equalsIgnoreCase( "texas" ) ) {
            presets.put( "getVotingAddress.setState", "TX" );
        }
        if ( name.equalsIgnoreCase( "vermont" ) ) {
            presets.put( "getVotingAddress.setState", "VT" );
        }
        if ( name.equalsIgnoreCase( "newyork" ) ) {
            presets.put( "getVotingAddress.setState", "NY" );
        }
        return presets;
    }
}
