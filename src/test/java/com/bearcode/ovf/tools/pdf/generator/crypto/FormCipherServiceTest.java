package com.bearcode.ovf.tools.pdf.generator.crypto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import javax.crypto.SecretKey;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Date: 15.10.14
 * Time: 17:32
 *
 * @author Leonid Ginzburg
 */
public class FormCipherServiceTest extends EasyMockSupport {

    private EncryptionKeysCache keysCache;

    private EncryptionKeysCache keysCacheMock;

    private CipherService cipherService;

    @Before
    public void setUpCipherService() {
        keysCache = new EncryptionKeysCache();
        keysCacheMock = createMock("KeyCache", EncryptionKeysCache.class );
        cipherService = new CipherService();
        ReflectionTestUtils.setField(cipherService, "keysCache", keysCacheMock);
    }

    @After
    public void tearDownEncryptionHelper() {
        keysCache = null;
        keysCacheMock = null;
        cipherService = null;
    }

    @Test
    public void testEncryptAndDecrypt() throws CipherServiceException, UnsupportedEncodingException {
        final SecretKey key = keysCache.createEncryptionKey( 99l );
        assertNotNull(key);

        EasyMock.expect( keysCacheMock.createEncryptionKey(EasyMock.anyLong())).andReturn( key ).anyTimes();
        EasyMock.expect( keysCacheMock.getEncryptionKey(EasyMock.anyLong())).andReturn( key ).anyTimes();
        replayAll();

        final String source = "This is just a test text for testing encryption/decryption process.";

        final ByteArrayInputStream inputStream = new ByteArrayInputStream( source.getBytes("UTF-8") );
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        cipherService.encrypt( inputStream, outputStream, 99l );

        ByteArrayInputStream encodedInputStream = new ByteArrayInputStream( outputStream.toByteArray() );
        ByteArrayOutputStream encodedOutputStream = new ByteArrayOutputStream();
        cipherService.decrypt( encodedInputStream, encodedOutputStream, 99l );

        final String decoded = encodedOutputStream.toString("UTF-8");
        assertEquals("Strings before encoding and after decoding should be equal", source, decoded );
        verifyAll();
    }
}
