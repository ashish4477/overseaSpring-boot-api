package com.bearcode.ovf.tools.pdf.generator.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Date: 09.10.14
 * Time: 0:16
 *
 * @author Leonid Ginzburg
 */
@Component
public class EncryptionKeysCache {

    @CachePut(value = "pdfKeys")
    public SecretKey createEncryptionKey( long generationId ) throws CipherServiceException {
        try {
            final KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(new SecureRandom());
            return kgen.generateKey();
        } catch (NoSuchAlgorithmException e) {
        	throw new CipherServiceException("Cannot create AES SecretKey", e);
        }
    }

    // just for get keys from the cache. if the cache has no such element there is nothing to return
    @Cacheable(value = "pdfKeys")
    public SecretKey getEncryptionKey( long generationId ) {
        return null;
    }

}
