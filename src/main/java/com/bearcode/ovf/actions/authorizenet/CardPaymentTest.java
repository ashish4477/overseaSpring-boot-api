package com.bearcode.ovf.actions.authorizenet;

import com.bearcode.ovf.actions.authorizenet.forms.CardAuthorizeForm;
import com.bearcode.ovf.actions.commons.OverseasFormController;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: May 20, 2008
 * Time: 2:17:16 PM
 * @author Leonid Ginzburg
 */
public class CardPaymentTest extends OverseasFormController {
    public Map buildReferences(HttpServletRequest request, Object object, Errors errors) throws Exception {
        return null;  
    }


    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        CardAuthorizeForm cardForm = (CardAuthorizeForm) command;

        HttpClient httpClient = new HttpClient();
        GetMethod method = new GetMethod( "https://test.authorize.net/gateway/transact.dll"  );
        NameValuePair[] pairs = new NameValuePair[13];

        pairs[0] = new NameValuePair("x_login", "6zz6m5N4Et");         //"933hKU49rpd7"
        pairs[1] = new NameValuePair("x_tran_key", "9V9wUv6Yd92t27t5");   //"6E4SV54Bkh6t7rkD"
        pairs[2] = new NameValuePair("x_version","3.1");

        pairs[3] = new NameValuePair("x_test_request","TRUE");  //todo change it for live application

        pairs[4] = new NameValuePair("x_method","CC");
        pairs[5] = new NameValuePair("x_type","AUTH_CAPTURE");
        pairs[6] = new NameValuePair("x_amount","" + cardForm.getAmount() );
        pairs[7] = new NameValuePair("x_delim_data","TRUE");
        pairs[8] = new NameValuePair("x_delim_char","|");
        pairs[9] = new NameValuePair("x_relay_response","FALSE");

        pairs[10] = new NameValuePair("x_card_num",cardForm.getCardNum() );
        pairs[11] = new NameValuePair("x_exp_date",cardForm.getCardExpired() );

        pairs[12] = new NameValuePair("x_description","Java Transaction");

        /*sb.append("x_login=yourloginid&");             // replace with your own
        sb.append("x_tran_key=eoXaDm2LUnz2OiyQ&");     // replace with your own
        sb.append("x_version=3.1&");
        sb.append("x_test_request=TRUE&");             // for testing
        sb.append("x_method=CC&");
        sb.append("x_type=AUTH_CAPTURE&");
        sb.append("x_amount=1.00&");
        sb.append("x_delim_data=TRUE&");
        sb.append("x_delim_char=|&");
        sb.append("x_relay_response=FALSE&");

        // CC information
        sb.append("x_card_num=4007000000027&");
        sb.append("x_exp_date=0509&");

        // not required...but my test account is set up to require it
        sb.append("x_description=Java Transaction&");*/

        method.setQueryString( pairs );

        httpClient.executeMethod(method);
        String responseString = IOUtils.toString( method.getResponseBodyAsStream(), "UTF-8" );

        return buildSuccessModelAndView( request, command, errors, "authResponse", parseResponseString( responseString, "\\|" ) );
        //return super.onSubmit(request, response, command, errors);
    }

    private List<String> parseResponseString( String response, String delim ) {
        return Arrays.asList( response.split( delim ) );
    }
}
