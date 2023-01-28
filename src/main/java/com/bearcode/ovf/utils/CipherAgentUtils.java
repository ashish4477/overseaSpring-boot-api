package com.bearcode.ovf.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author leonid.
 */
public class CipherAgentUtils {

    // Salt
    protected static byte[] SALT = {
            (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
            (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99
    };

    public static String createToken( String key, String token) throws CipherAgentException {
        PBEKeySpec pbeKeySpec;
        PBEParameterSpec pbeParamSpec;
        SecretKeyFactory keyFac;

        // Iteration count
        int count = 20;
        byte[] cipherName = new byte[0];

        try {
            // Create PBE parameter set
            pbeParamSpec = new PBEParameterSpec( SALT, count );

            pbeKeySpec = new PBEKeySpec( key.toCharArray() );
            keyFac = SecretKeyFactory.getInstance( "PBEWithMD5AndDES" );
            SecretKey pbeKey = keyFac.generateSecret( pbeKeySpec );

            // Create PBE Cipher
            Cipher pbeCipher = Cipher.getInstance( "PBEWithMD5AndDES" );

            // Initialize PBE Cipher with key and parameters
            pbeCipher.init( Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec );

            //String clearCode = String.format( "%s:%x", token, new Date().getTime() );
            cipherName = pbeCipher.doFinal( token.getBytes() );
        } catch ( Exception e ) {
            throw new CipherAgentException( "Unable to encode authentication key", e );
        }
        BigInteger hash = new BigInteger( 1, cipherName );
        return String.format( "%x", hash );
    }

    public static String decodeToken( String token, String key ) throws CipherAgentException {
        PBEKeySpec pbeKeySpec;
        PBEParameterSpec pbeParamSpec;
        SecretKeyFactory keyFac;

        // Iteration count
        int count = 20;

        // Create PBE parameter set
        try {
            pbeParamSpec = new PBEParameterSpec( SALT, count );

            pbeKeySpec = new PBEKeySpec( key.toCharArray() );
            keyFac = SecretKeyFactory.getInstance( "PBEWithMD5AndDES" );
            SecretKey pbeKey = keyFac.generateSecret( pbeKeySpec );

            // Create PBE Cipher
            Cipher pbeCipher = Cipher.getInstance( "PBEWithMD5AndDES" );

            // Initialize PBE Cipher with key and parameters
            pbeCipher.init( Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec );

            BigInteger hash = new BigInteger( token, 16 );
            byte[] hashBA = hash.toByteArray();
            if ( hashBA.length % 8 != 0 ) {   // must be multiple of 8
                // truncate leading '0'
                hashBA = Arrays.copyOfRange( hashBA, 1, hashBA.length );
            }
            String clearName = new String( pbeCipher.doFinal( hashBA ) );

            //String[] parts = clearName.split( ":" );
            return clearName;
        } catch (Exception e) {
            throw new CipherAgentException( "Unable to decode authentication key", e );
        }
    }
}
