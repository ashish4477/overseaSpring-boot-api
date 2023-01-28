package com.bearcode.ovf.webservices.votesmart;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Date: 27.09.14
 * Time: 0:25
 *
 * @author Leonid Ginzburg
 */
@Component
public class VoteSmartConnector {

    private static final String VS_API_URL = "http://api.votesmart.org/";
    private static final String VS_API_OUTPUT_FORMAT = "xml"; // supported format by voteSmart JSON/XML

    private static Logger logger = LoggerFactory.getLogger( VoteSmartConnector.class );

    @Value("${voteSmart.authKey}")
    private String voteSmartKey;

    public void setVoteSmartKey( String voteSmartKey ) {
        this.voteSmartKey = voteSmartKey;
    }

    // execute VS API request
    @Cacheable(value = "votesmart", key = "#key" )
    private String execute( final String method, List<NameValuePair> args, String key ) {
        try {
            HttpClient httpClient = new HttpClient();
            GetMethod m = new GetMethod( VS_API_URL + method );
            m.setQueryString( args.toArray( new NameValuePair[args.size()] ) );
            httpClient.executeMethod( m );
            return IOUtils.toString( m.getResponseBodyAsStream(), "UTF-8" );
        } catch (IOException e) {
            logger.error( "VoteSmart: could not connect " + VS_API_URL + method, e );
        }
        return null;
    }

    public String getVoteSmartInfo( VoteSmartMethodMeta method, Map<String,String> model ) {
        // create default request arguments
        final List<NameValuePair> args = createDefaultArgs();
        StringBuilder keyBuilder = new StringBuilder( method.getMethod() );
        for ( String key : model.keySet() ) {
            args.add( new NameValuePair( key, model.get( key ) ) );
            keyBuilder.append( key ).append( model.get( key ) );
        }
        String cacheKey = DigestUtils.md5DigestAsHex(keyBuilder.toString().getBytes());

        return execute( method.getMethod(), args, cacheKey );
    }

    private List<NameValuePair> createDefaultArgs() {
        LinkedList<NameValuePair> pairs = new LinkedList<NameValuePair>();
        pairs.add( new NameValuePair( "key", voteSmartKey) );
        pairs.add( new NameValuePair( "o", VS_API_OUTPUT_FORMAT ) );
        return pairs;
    }

}
