package com.bearcode.ovf.forms;

import java.util.Date;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Oct 23, 2007
 * Time: 6:09:52 PM
 * @author Leonid Ginzburg
 */
public class UserFilterForm extends CommonFormObject {
    private String login = "";
    private String firstName = "";
    private String lastName = "";
    private long roleId = 0;
    private Date minLastUpdated = new Date(0);
	private Date maxLastUpdated = new Date();


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

	public Date getMinLastUpdated() {
		return minLastUpdated;
	}

	public void setMinLastUpdated(Date minLastUpdated) {
		this.minLastUpdated = minLastUpdated;
	}

	public Date getMaxLastUpdated() {
		return maxLastUpdated;
	}

	public void setMaxLastUpdated(Date maxLastUpdated) {
		this.maxLastUpdated = maxLastUpdated;
	}


    public Map getPagingParams() {
        Map data = super.getPagingParams();
        if ( login.length() > 0 ) data.put("login", login );
        if ( firstName.length() > 0 ) data.put("firstName", firstName );
        if ( lastName.length() > 0 ) data.put("lastName", lastName );
        if ( roleId > 0 ) data.put("roleId", roleId );
        return data;
    }
}
