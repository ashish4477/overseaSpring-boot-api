package com.bearcode.ovf.actions.commons;

import com.bearcode.commons.util.MapUtils;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daemmon Hughes
 * 
 * 
 * 
 */
public class IframeContent extends OverseasFormController {
    private String iframeURL;
    private String iframeHeader;


    public void setIframeURL(String iframeURL) {
        this.iframeURL = iframeURL;
    }

    public void setIframeHeader(String str) {
        this.iframeHeader = str;
    }

    public Map buildReferences(HttpServletRequest request, Object object, Errors errors) throws Exception {
        HashMap<String,Object> data =  new HashMap<String, Object>();
        
        String url = MapUtils.getString(request.getParameterMap(), "redirectURL", "http://195.53.103.187:8080/diaspora/index.action");
        String login = MapUtils.getString(request.getParameterMap(), "login", "voter");
        String voting_region = MapUtils.getString(request.getParameterMap(), "voting_region", "TestCounty1");
        String state = MapUtils.getString(request.getParameterMap(), "state", "TEST_STATE_1");

        //TODO remove this testing code
        iframeURL = "/bogus?redirectURL="+url+"&login="+login+"&voting_region="+voting_region+"&state="+state;

        data.put("iframeURL", iframeURL);        
        data.put("iframeHeader", iframeHeader);        
        return data;
    }
}
