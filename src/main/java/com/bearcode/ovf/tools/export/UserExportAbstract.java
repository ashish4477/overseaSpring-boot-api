package com.bearcode.ovf.tools.export;

import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.service.OverseasUserService;
import com.bearcode.ovf.utils.UserInfoFields;

import java.io.OutputStream;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dhughes
 * Date: Feb 7, 2011
 * Time: 5:19:04 PM
 * To change this template use File | Settings | File Templates.
 */
abstract public class UserExportAbstract {

	protected OverseasUserService userService;

    public void setOverseasUserService(OverseasUserService userService) {
        this.userService = userService;
    }

	protected String[] getRavaColumnTitles() {

		       	return new String[] {
        		UserInfoFields.FIRST_NAME,
	       		UserInfoFields.MIDDLE_NAME,
    	   		UserInfoFields.LAST_NAME,

        		UserInfoFields.VOTING_ADDRESS,
        		UserInfoFields.VOTING_CITY,
        		UserInfoFields.VOTING_STATE,
        		UserInfoFields.VOTING_ZIP,
        		UserInfoFields.VOTING_REGION,
        		UserInfoFields.CURRENT_ADDRESS,
        		UserInfoFields.CURRENT_CITY,
         		UserInfoFields.CURRENT_STATE,
        		UserInfoFields.CURRENT_POSTAL_CODE,
        		UserInfoFields.CURRENT_COUNTRY,
	       		UserInfoFields.PHONE,
	       		UserInfoFields.EMAIL,

        		UserInfoFields.VOTER_TYPE,
        		UserInfoFields.BALLOT_PREF,
	       		UserInfoFields.PARTY,
         		UserInfoFields.BIRTH_MONTH,
       			UserInfoFields.BIRTH_YEAR,
	       		UserInfoFields.RACE,
	       		UserInfoFields.ETHNICITY,

        		UserInfoFields.FORWARD_ADDRESS,
        		UserInfoFields.FORWARD_POSTAL_CODE,
        		UserInfoFields.FORWARD_CITY,
        		UserInfoFields.FORWARD_STATE,
        		UserInfoFields.FORWARD_COUNTRY,
        };
	}

	/*
	protected String[] getRavaColumnNames() {

	   	Map<String,String> userFields = UserInfoFields.getFieldsMap();

		String[] titles = getRavaColumnTitles();
		int cols = titles.length;
		String[] outNames = new String[cols];
		for(int i=0;i<cols;i++){
			outNames[i] = userFields.get(titles[i]);
 		}
		return outNames;
	}
	*/

	protected Map<String,String> getUserValues(OverseasUser user){
	   	return UserInfoFields.getUserValues(user);
	}

	abstract public void write(OutputStream out, int start, int limit) throws Exception;

	
}
