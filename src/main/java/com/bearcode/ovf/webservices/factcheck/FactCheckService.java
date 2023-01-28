package com.bearcode.ovf.webservices.factcheck;

import com.bearcode.ovf.webservices.factcheck.model.Article;
import com.bearcode.ovf.webservices.factcheck.model.ArticlesList;
import com.bearcode.ovf.webservices.factcheck.model.TagsList;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * Date: 27.05.14
 * Time: 16:33
 *
 * @author Leonid Ginzburg
 */
@Component
public class FactCheckService {
    private final Logger logger = LoggerFactory.getLogger(FactCheckService.class);

    private String serviceBaseUrl = "http://mva.usvotefoundation.org";
    private String methodPathPrefix = "/api/v1/";
    private String authParamName = "oauth_consumer_key";

    @Value("${FACT_CHECK_KEY}")
    private String authKey = "NOT-SET";

    public static final String LOCATIONS = "locations";
    public static final String ARTICLES = "articles";
    public static final String ISSUES = "issues";
    public static final String CATEGORIES = "categories";
    public static final String PEOPLE = "people";
    public static final String TAGS = "tags";
    public static final String ARTICLES_SEARCH = "articles/search";

    public static final String LIMIT_PARAM = "limit=%d";
    public static final String ISSUES_PARAM = "issues=%s";
    public static final String LOCATIONS_PARAM = "locations=%s";
    public static final String YEAR_PARAM = "published_at__range=%s-01-01,%s-12-31";
    private static final String JSON_PARAM = "format=json";

    private static final String METHOD_PATH_2_PATTERN = "%s/%d";
    private static final String METHOD_PATH_3_PATTERN = "%s/%d/%s";

    public void setServiceBaseUrl(String serviceBaseUrl) {
        this.serviceBaseUrl = serviceBaseUrl;
    }

    public void setMethodPathPrefix(String methodPathPrefix) {
        this.methodPathPrefix = methodPathPrefix;
    }

    public void setAuthParamName(String authParamName) {
        this.authParamName = authParamName;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public <T> T getObjects( final String requestedUrl, Class<T> type) {

        final String uri = buildRequestedUri( requestedUrl );
        GetMethod method = new GetMethod( uri );
        Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();
        // new instance of HttpClient.
        HttpClient client = new HttpClient();
        T result = null;
        try {
            int statusCode = client.executeMethod(method);

            if (statusCode == HttpStatus.SC_OK) {
                try {
                    Reader reader = new InputStreamReader( method.getResponseBodyAsStream(), "UTF-8" );
                    result = gson.fromJson( reader, type );
                } catch (JsonSyntaxException e) {
                    logger.error(String.format("FactCheck: failed to parse result of request: %s ", requestedUrl), e);
                }
            } else {
                logger.error( String.format( "FactCheck: failed to connect server; message: %s ", method.getStatusLine() ) );
            }

        } catch (HttpException e) {
            logger.error( "FactCheck: Fatal protocol violation: " + e.getMessage() );
        } catch (IOException e) {
            logger.error( "FactCheck: Fatal transport error: " + e.getMessage() );
        } finally {
            // release the connection.
            method.releaseConnection();
        }

        return result;
    }

    private String buildRequestedUri( String uri ) {
        return String.format("%s%s", serviceBaseUrl, uri);
    }

    private String buildRequestedUrl( String methodPath, String params ) {
        return String.format("%s%s?%s", methodPathPrefix, methodPath, params);
    }

    private String buildRequestedUrl( String methodTag, Long id, String params ) {
        String methodPath  = String.format( METHOD_PATH_2_PATTERN, methodTag, id );
        return buildRequestedUrl( methodPath, params );
    }

    private String buildRequestedUrl( String methodTag, Long id, String methodSubTag, String params ) {
        String methodPath  = String.format( METHOD_PATH_3_PATTERN, methodTag, id, methodSubTag );
        return buildRequestedUrl( methodPath, params );
    }

    private void addRequiredParams( Collection<String> params ) {
        boolean addJson = true;
        boolean addOauth = !authParamName.isEmpty() && !authKey.isEmpty();
        for ( String param : params ) {
            if ( addJson && param.contains(JSON_PARAM) ) addJson = false;
            if ( addOauth && param.contains(authParamName) ) addOauth = false;
        }
        if ( addJson ) params.add(JSON_PARAM);
        if ( addOauth ) {
            params.add( String.format("%s=%s", authParamName, authKey) );
        }
    }

    private String buildRequestedParam() {
        Collection<String> params = new HashSet<String>();
        return buildRequestedParam( params );
    }

    private String buildRequestedParam( String param ) {
        Collection<String> params = new HashSet<String>();
        params.add( param );
        return buildRequestedParam(params);
    }

    private String buildRequestedParam( Collection<String> params ) {
        addRequiredParams( params );
        return StringUtils.join(params, "&");
    }

    public ArticlesList getArticlesOfTag( String tagName, long id ) {
        String requestedUrl = buildRequestedUrl(tagName, id, ARTICLES, buildRequestedParam());
        return getObjects(requestedUrl, ArticlesList.class);
    }

    public ArticlesList getArticlesOfTag( String tagName, long id, int limit ) {
        String requestedUrl = buildRequestedUrl(tagName, id, ARTICLES, buildRequestedParam(String.format(LIMIT_PARAM, limit )));
        return getObjects( requestedUrl, ArticlesList.class );
    }

    public ArticlesList getArticlesSearch( Collection<String> params ) {
        String requestedUrl = buildRequestedUrl(ARTICLES_SEARCH, buildRequestedParam(params));
        return getArticlesMore(requestedUrl);
    }

    @Cacheable( value = "getAllTags" )
    public TagsList getAllTags( String tagName ) {
        String requestedUrl = buildRequestedUrl( tagName, buildRequestedParam() );
        TagsList list = getObjects( requestedUrl, TagsList.class ) ;
        if ( list == null || list.getMeta() == null || list.getMeta().getLimit() >= list.getMeta().getTotalCount() ) {
            return list;
        }
        requestedUrl = buildRequestedUrl( tagName, buildRequestedParam( String.format(LIMIT_PARAM, list.getMeta().getTotalCount() ) ) );
        return getObjects( requestedUrl, TagsList.class );
    }

    @Cacheable( value = "getArticle" )
    public Article getArticle( Long id ) {
        String requestedUrl = buildRequestedUrl( ARTICLES, id, buildRequestedParam() );
        return getObjects( requestedUrl, Article.class );
    }

    @Cacheable( value = "getArticlesMore" )
    public ArticlesList getArticlesMore( String url ) {
        return getObjects( url, ArticlesList.class );
    }

    public List<Article> getOrderedArticles( final Article[] articles ) {
        List<Article> articleList = new LinkedList<Article>();
        Collections.addAll( articleList, articles );
        Collections.sort( articleList, new Comparator<Article>() {
            @Override
            public int compare(Article a1, Article a2) {
                return a2.getPublishedAt().compareTo( a1.getPublishedAt() );
            }
        });
        return articleList;
    }
}
