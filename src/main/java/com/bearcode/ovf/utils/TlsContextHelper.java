package com.bearcode.ovf.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

/**
 * @author Daemmon Hughes
 * @date 7/26/12
 */
public class TlsContextHelper{

    protected static Logger logger = LoggerFactory.getLogger(TlsContextHelper.class);
    private static SSLContext defaultSSLContext;
    static {
        try {
            defaultSSLContext = SSLContext.getDefault();
        } catch ( NoSuchAlgorithmException e ) {
            logger.info( "Exception using CustomTrustManager", e );
        }
    }

    public static void useCustomTrustManager(){

        /*
              Use a custom trust manager to trust self-signed certs
        */
        try {
                SSLContext ctx = SSLContext.getInstance( "TLS" );
                ctx.init( new KeyManager[0], new TrustManager[]{new CustomTrustManager()}, new SecureRandom() );
                SSLContext.setDefault( ctx );
            } catch ( Exception e ) {
                logger.info( "Exception using CustomTrustManager", e );
            }
    }

    public static void useDefaultTrustManager(){
        /*
              Use the default trust manager
        */
        SSLContext.setDefault( defaultSSLContext );
    }
    /**
     * Trust manager that will accept unsigned SSL certs
     */
    private static class CustomTrustManager implements X509TrustManager{


        @Override
        public void checkClientTrusted( java.security.cert.X509Certificate[] x509Certificates, String s ) throws CertificateException{
        }

        @Override
        public void checkServerTrusted( java.security.cert.X509Certificate[] x509Certificates, String s ) throws CertificateException {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

}
