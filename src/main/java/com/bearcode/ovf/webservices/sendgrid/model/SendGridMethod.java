package com.bearcode.ovf.webservices.sendgrid.model;

import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.Map;

/**
 * @author leonid.
 */
public enum SendGridMethod {
    ADD_RECIPIENTS("/contactdb/recipients", RequestMethod.POST, true, false ),
    DELETE_RECIPIENTS("/contactdb/recipients", RequestMethod.DELETE, false, false ),
    GET_UNSUBSCRIBES("/suppression/unsubscribes", RequestMethod.GET, true, true ),
    DELETE_UNSUBSCRIBES("/asm/suppressions/global/{email}", RequestMethod.DELETE, false, false),
    GET_SUPPRESSION_GROUPS("/asm/suppressions/{email}", RequestMethod.GET, true, false ),
    DELETE_SUPPRESSIONS("/asm/groups/{group_id}/suppressions/{email}", RequestMethod.DELETE, false, false);

    private String methodUri;
    private RequestMethod requestMethod;
    /**
     * Expect Response Body after API call
     */
    private boolean expectResponseBody;
    /**
     * Add params into query string
     */
    private boolean queryStringParams;

    SendGridMethod( String methodUri, RequestMethod requestMethod,
                    boolean expectResponseBody, boolean queryStringParams ) {
        this.methodUri = methodUri;
        this.requestMethod = requestMethod;
        this.expectResponseBody = expectResponseBody;
        this.queryStringParams = queryStringParams;
    }

    public String getMethodUri() {
        return getMethodUri( Collections.<String, String>emptyMap() );
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public boolean isExpectResponseBody() {
        return expectResponseBody;
    }

    public boolean isQueryStringParams() {
        return queryStringParams;
    }

    public String getMethodUri( Map<String, String> params ) throws IllegalArgumentException {
        String uri = methodUri;
        if ( params != null && !params.isEmpty() ) {
            for ( String key : params.keySet() ) {
                uri = uri.replace( "{" + key + "}", params.get( key ) );
            }
        }
        if ( uri.contains( "{" ) ) {
            throw new IllegalArgumentException( "Params map doesn't contain all required values" );
        }
        return uri;
    }
}
