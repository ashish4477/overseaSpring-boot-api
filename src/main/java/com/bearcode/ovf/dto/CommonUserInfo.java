package com.bearcode.ovf.dto;

/**
 * Created by IntelliJ IDEA.
 * User: dhughes
 * Date: Aug 3, 2010
 * Time: 3:19:09 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Data transfer object that extracts common datafields from an OverseasUser and a WizardContext object
 */
public class CommonUserInfo {
/*
    private Map <String,String> fields = UserInfoFields.getFieldsMap();
    private Map <String,Object> values;
    private OverseasUser user;
    private PdfAnswers form;

    public CommonUserInfo(OverseasUser usr, PdfAnswers frm){

        form = frm;
        values = form.createModel();
        user = usr;
    }

    public String getFirstName(){
        return user.getFirstName();
    }

    public String getLastName(){
        return user.getLastName();
    }

    public String getPassword(){
        return user.getPassword();
    }

    public String getScytlPassword(){
        return user.getScytlPassword();
    }

    public String getEmail(){
        return user.getEmail();
    }

    public String getState(){
        return getValue(UserInfoFields.STATE);
    }

    public String getCity(){
        return getValue(UserInfoFields.CITY);
    }

    public String getAddress(){
        return getValue(UserInfoFields.ADDRESS);
    }

    public String getZip(){
        return getValue(UserInfoFields.ZIP);
    }

    public String getIdNumber(){
       return getValue(UserInfoFields.ID_NUMBER);
    }

    public String getVotingRegion(){
        return getValue(UserInfoFields.VOTING_REGION);
    }

    public String getPhone(){
        return getValue(UserInfoFields.PHONE);
    }

    public String getVoterType(){
        return getValue(UserInfoFields.VOTER_TYPE);
    }

    public String getBirthdayString(){

        return getValue(UserInfoFields.BIRTH_YEAR)+
                getValue(UserInfoFields.BIRTH_MONTH)+
                getValue(UserInfoFields.BIRTH_DATE);
    }

    private String getValue(String field){

        String value = "";
        String fieldList = fields.get(field);
        for(String f : fieldList.split(",")){
            f = f.replaceAll(" ","");
            String fvalue = (String)values.get(f);
            if(fvalue != null){
                if(value.length() > 0){
                    value += " "; // add a space between multiple values 
                }
                value += fvalue;
            }
        }       
        return value;
    }


    /**
     * Chack if overlapping fields in user an form objects match: firsname. lastname, email
     * Also make sure all of the answers in the form belong to the user
     *
     * @return boolean
     *
    public boolean isValid(){

        boolean ret = true;
        if(!form.getUser().equals(user)){
        	ret = false;
        }
        if(!((String)values.get(fields.get(UserInfoFields.FIRST_NAME)))
                .equalsIgnoreCase(user.getFirstName())){
            ret = false;
        }
        if(!((String)values.get(fields.get(UserInfoFields.LAST_NAME)))
                .equalsIgnoreCase(user.getLastName())){
            ret = false;
        }
        String formEmail = (String)values.get(fields.get(UserInfoFields.EMAIL));
        if(!(formEmail.equalsIgnoreCase(user.getEmail()) || formEmail.equalsIgnoreCase(user.getUsername()))){
            ret = false;
        }

        return ret;
    }
*/
}
