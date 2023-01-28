package com.bearcode.ovf.tools;

import com.bearcode.ovf.actions.express.forms.ExpressForm;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 8, 2008
 * Time: 6:59:25 PM
 * @author Leonid Ginzburg
 */
public class AuthorizeNetService {
    public static final String DELIMITER = "|";

    private String serviceUrl = "https://test.authorize.net/gateway/transact.dll";
    private String serviceLogin = "6zz6m5N4Et";
    private String serviceKey = "9V9wUv6Yd92t27t5";
    private boolean testRequest = true;


    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public void setServiceLogin(String serviceLogin) {
        this.serviceLogin = serviceLogin;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    public void setTestRequest(boolean testRequest) {
        this.testRequest = testRequest;
    }

    public boolean doPayment( ExpressForm form, Double amount, String clientIP ) {
        HttpClient httpClient = new HttpClient();
        GetMethod method = new GetMethod( serviceUrl );
        /*NameValuePair[]*/
        List<NameValuePair> pairs = new LinkedList<NameValuePair>();

        pairs.add( new NameValuePair("x_login", serviceLogin ));
        pairs.add( new NameValuePair("x_tran_key", serviceKey ));
        pairs.add( new NameValuePair("x_version", "3.1"));

        if ( testRequest ) {
            pairs.add( new NameValuePair("x_test_request", "TRUE"));
        }

        pairs.add( new NameValuePair("x_method", "CC"));
        pairs.add( new NameValuePair("x_type", "AUTH_CAPTURE"));
        pairs.add( new NameValuePair("x_amount", "" + amount));
        pairs.add( new NameValuePair("x_delim_data", "TRUE"));
        pairs.add( new NameValuePair("x_delim_char", DELIMITER));
        pairs.add( new NameValuePair("x_relay_response", "FALSE"));

        pairs.add( new NameValuePair("x_card_num", form.getCcNumber()));
        pairs.add( new NameValuePair("x_exp_date", form.getCardExpired()));
        pairs.add( new NameValuePair("x_card_code", form.getCvv()));

        // billing address
        String address;
        String city;
        String state;
        String zip;
        String country;
        if ( form.getBilling().getStreet1().trim().length() > 0 ) {
            address = form.getBilling().getStreet1() + " " + form.getBilling().getStreet2();
            address = address.trim();
            city = form.getBilling().getCity();
            state = form.getBilling().getState();
            zip = form.getBilling().getZip();
            country = form.getBillingCountry();
        }
        else {
            address = form.getPickUp().getStreet1() + " " + form.getPickUp().getStreet2();
            address = address.trim();
            city = form.getPickUp().getCity();
            state = form.getPickUp().getState();
            zip = form.getPickUp().getZip();
            country = form.getCountry().getName();
        }

        pairs.add( new NameValuePair("x_address", address.length() <= 60 ? address : address.substring(0,60) ) );
        pairs.add( new NameValuePair("x_city", city.length() <= 40 ? city : city.substring(0,60) ) );
        pairs.add( new NameValuePair("x_state", state.length() <= 40 ? state : state.substring(0,60) ) );
        pairs.add( new NameValuePair("x_zip", zip.length() <= 20 ? zip : zip.substring(0,60) ) );
        pairs.add( new NameValuePair("x_country", country.length() <= 60 ? country : country.substring( 0, 60 ) ) );

        pairs.add( new NameValuePair("x_description", "EYV Air Way Bill " + form.getFedexLabel().getTrackingNumber()));
        pairs.add( new NameValuePair("x_email", form.getNotificationEmail()));
        pairs.add( new NameValuePair("x_phone", form.getNotificationPhone()));

        pairs.add( new NameValuePair("x_first_name", form.getFirstName()));
        pairs.add( new NameValuePair("x_last_name", form.getLastName()));

        pairs.add( new NameValuePair("x_customer_ip", clientIP ));

        method.setQueryString( pairs.toArray( new NameValuePair[pairs.size()]  ));

        String responseString;
        try {
            httpClient.executeMethod(method);
            responseString = IOUtils.toString( method.getResponseBodyAsStream(), "UTF-8" );
        } catch (IOException e) {
            form.setAuthorizenetMessage("Can't connect Authorize Net");
            return false;
        }

        // response documentation:
        // http://developer.authorize.net/guides/AIM/wwhelp/wwhimpl/common/html/wwhelp.htm#href=4_TransResponse.Response%20Code%20Details.html#1094159&single=true
        String[] parsed = responseString.split( "\\" + DELIMITER );
        form.getFedexLabel().setTransaction( parsed[6] );
        form.setAuthorizenetMessage( parsed[3] );
        if ( parsed[0].equals("1") || ( parsed[0].equals("4") && parsed[2].equals("253") ) ) {
            return true;
        }
        return false;
    }
}
