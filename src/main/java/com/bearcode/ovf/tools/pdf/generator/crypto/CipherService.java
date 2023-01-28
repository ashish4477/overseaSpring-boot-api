package com.bearcode.ovf.tools.pdf.generator.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Date: 08.10.14
 * Time: 18:33
 *
 * @author Leonid Ginzburg
 */
@Service
public class CipherService {

    @Autowired
    private EncryptionKeysCache keysCache;

    public void encrypt( InputStream input, OutputStream output, long generationId ) throws CipherServiceException {
        final SecretKey key = keysCache.createEncryptionKey( generationId );
        if ( key == null ) {
            return;
        }

        CipherOutputStream cipherOutputStream = null;
        try {
            final Cipher cipher = Cipher.getInstance( "AES" );
            cipher.init( Cipher.ENCRYPT_MODE, key );

            cipherOutputStream = new CipherOutputStream( output, cipher );
            IOUtils.copy( input, cipherOutputStream );

        } catch (GeneralSecurityException e) {
            throw new CipherServiceException("Cannot encrypt", e);
        } catch (IOException e) {
            throw new CipherServiceException("Cannot encrypt", e);
        } finally {
        	IOUtils.closeQuietly( cipherOutputStream );
        }
    }

    public void decrypt( InputStream input, OutputStream output, long generationId ) throws CipherServiceException {
        final SecretKey key = keysCache.getEncryptionKey( generationId );
        if ( key == null ) {
            return;
        }

        CipherInputStream cipherInputStream = null;
        try {
            final Cipher cipher = Cipher.getInstance( "AES" );
            cipher.init( Cipher.DECRYPT_MODE, key );

            cipherInputStream = new CipherInputStream( input, cipher );
            IOUtils.copy( cipherInputStream, output );

        } catch (GeneralSecurityException e) {
            throw new CipherServiceException("Cannot decrypt", e);
        } catch (IOException e) {
            throw new CipherServiceException("Cannot decrypt", e);
        } finally {
        	IOUtils.closeQuietly( cipherInputStream );
        }
    }
}
