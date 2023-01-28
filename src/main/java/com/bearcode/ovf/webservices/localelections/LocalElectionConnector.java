package com.bearcode.ovf.webservices.localelections;

import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.OvfPropertyService;
import com.bearcode.ovf.webservices.util.BaseRestfulApiConnector;
import com.google.gson.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

/**
 * Created by leonid on 01.06.16.
 *
 * Component for making call to localelection.usvotefoundation API
 */
@Component
public class LocalElectionConnector extends BaseRestfulApiConnector {
    private static Logger logger = LoggerFactory.getLogger( LocalElectionConnector.class );

    public final static String GET_STATES = "states";
    public final static String GET_ELECTIONS = "elections";
    public final static String GET_STATE_VOTER_INFORMATION = "state_voter_information";

    @Value( "${LOCAL_ELECTION_URL}" )
    private String serviceUrl;

    @Value( "${LOCAL_ELECTION_AUTH_TOKEN:NOT-SET}")
    private String authorizationToken;

    @Autowired
    private OvfPropertyService propertyService;

    @Autowired
    private Environment env;

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public String getServiceUrl() {
        return System.getenv("LOCAL_ELECTION_URL");
    }

    @Override
    public String getAuthorizationToken() {
        return propertyService.getProperty( OvfPropertyNames.LOCAL_ELECTION_AUTH_TOKEN );
    }


    @PostConstruct
    private void postInit() {
        setDebugFormattedJSON( Boolean.parseBoolean( env.getProperty( "localelection.debugFormattedJSON", "false" ) ) );
        setDebugNoCache( Boolean.parseBoolean( env.getProperty( "localelection.debugNoCache", "false" ) ) );
    }


}
