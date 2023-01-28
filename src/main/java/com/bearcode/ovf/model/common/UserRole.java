package com.bearcode.ovf.model.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA. User: Leo Date: Jul 5, 2007 Time: 9:27:55 PM
 *
 * @author Leonid Ginzburg
 */
public class UserRole implements GrantedAuthority, Serializable {

    public static final String USER_ROLE_ADMIN = "admin";
    public static final String USER_ROLE_VOTER = "voter";
    public static final String USER_ROLE_FACE_ADMIN = "face_admin";
    public static final String USER_ROLE_USER_EXPORT = "user_export";
    public static final String USER_ROLE_REPORTING_DASHBOARD = "reporting_dashboard";
    public static final String USER_ROLE_APPLICATION_TESTER = "application_tester";
    public static final String USER_ROLE_PENDING_VOTER_REGISTRATIONS = "pending_voter_registrations";

    /**
     * ordered collection of roles
     */
    public static final String[] ALL_ROLES = new String[]{USER_ROLE_VOTER, USER_ROLE_REPORTING_DASHBOARD,
            USER_ROLE_APPLICATION_TESTER, USER_ROLE_FACE_ADMIN, USER_ROLE_USER_EXPORT, USER_ROLE_ADMIN, USER_ROLE_PENDING_VOTER_REGISTRATIONS };

    private static final long serialVersionUID = -4787804483969851237L;

    private long id;
    private String roleName;

    @Override
    @JsonIgnore
    public String getAuthority() {
        return "ROLE_" + roleName.toUpperCase();
    }

    public long getId() {
        return id;
    }

    public void setId( final long id ) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName( final String roleName ) {
        this.roleName = roleName;
    }
}
