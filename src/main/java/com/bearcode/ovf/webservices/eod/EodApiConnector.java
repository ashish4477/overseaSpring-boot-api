package com.bearcode.ovf.webservices.eod;

import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.OvfPropertyService;
import com.bearcode.ovf.webservices.util.BaseRestfulApiConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Connector to EOD API
 */
@Component
public class EodApiConnector extends BaseRestfulApiConnector {
    private static Logger logger = LoggerFactory.getLogger( EodApiConnector.class );

    public final static String GET_STATES = "states";
    public final static String GET_REGIONS = "regions";
    public final static String GET_OFFICES = "offices";
    public final static String GET_OFFICIALS = "officials";

    @Value( "${EOD_API_SERVICE_URL}" )
    private String serviceUrl;

    @Value( "${LOCAL_ELECTION_AUTH_TOKEN}")
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
        return System.getenv("EOD_API_SERVICE_URL");
    }

    @Override
    public String getAuthorizationToken() {
        return propertyService.getProperty( OvfPropertyNames.EOD_API_AUTH_TOKEN );
    }


    @PostConstruct
    private void postInit() {
        setDebugFormattedJSON( Boolean.parseBoolean( env.getProperty( "eodapi.debugFormattedJSON", "false" ) ) );
        setDebugNoCache( Boolean.parseBoolean( env.getProperty( "eodapi.debugNoCache", "false" ) ) );
    }


}
