/**
 * 
 */
package com.bearcode.ovf.service;

import com.bearcode.ovf.DAO.EncryptionKeyStatusDAO;
import com.bearcode.ovf.model.encryption.EncryptionKeyStatus;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;

import javax.crypto.Cipher;
import javax.servlet.ServletContext;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * Test for {@link EncryptionService}.
 * 
 * @author IanBrown
 * 
 * @since Nov 5, 2012
 * @version Nov 8, 2012
 */
public final class EncryptionServiceTest extends EasyMockSupport {

	/**
	 * the encryption DAO.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private EncryptionKeyStatusDAO encryptionDAO;

	/**
	 * the encryption service to test.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private EncryptionService encryptionService;

	/**
	 * the servlet context.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private ServletContext servletContext;

	/**
	 * Sets up the encryption service to test.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	@Before
	public final void setUpEncryptionService() {
		setEncryptionDAO(createMock("EncryptionDAO", EncryptionKeyStatusDAO.class));
		setEncryptionService(createEncryptionService());
		getEncryptionService().setEncryptionDAO(getEncryptionDAO());
	}

	/**
	 * Tears down the encryption service after testing.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	@After
	public final void tearDownEncryptionService() {
		setEncryptionService(null);
		setEncryptionDAO(null);
		setServletContext(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.EncryptionService#acquireDecryptionKey(java.util.Date, String, String)} for
	 * the case where there is an existing one.
	 * 
	 * @author IanBrown
	 * 
	 * @throws NoSuchAlgorithmException
	 *             if there is no RSA algorithm available.
	 * @throws InvalidKeySpecException
	 *             if the key specification is invalid.
	 * @throws IOException
	 *             if there is a problem with a key file.
	 * @throws Exception
	 *             if the acquisition fails.
	 * @since Nov 5, 2012
	 * @version Nov 7, 2012
	 */
	@Test
	public final void testAcquireDecryptionKey_existingKey() throws Exception {
		final Date creationDate = new Date();
		final String state = "ST";
		final String votingRegion = "Voting Region";
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(creationDate);
		final int month = calendar.get(Calendar.MONTH);
		final int year = calendar.get(Calendar.YEAR);
		final File folder = new File("target/test-classes");
		getEncryptionService().setKeyFolderPath(folder.getAbsolutePath());
		final String date = year + "" + month;
		final File privateKeyFile = new File(folder, state + "-" + votingRegion + "-" + date + ".dky");
		final File publicKeyFile = new File(folder, state + "-" + votingRegion + "-" + date + ".eky");
		try {
			final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			final KeyPair keyPair = keyPairGenerator.genKeyPair();
			final PrivateKey privateKey = keyPair.getPrivate();
			final PublicKey publicKey = keyPair.getPublic();
			final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			final RSAPrivateKeySpec privateKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
			saveToFile(privateKeyFile, privateKeySpec.getModulus(), privateKeySpec.getPrivateExponent());
			final RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
			saveToFile(publicKeyFile, publicKeySpec.getModulus(), publicKeySpec.getPublicExponent());
			final EncryptionKeyStatus encryptionKey = createMock("EncryptionKey", EncryptionKeyStatus.class);
			EasyMock.expect(getEncryptionDAO().findByStateVotingRegionAndDate(state, votingRegion, date)).andReturn(encryptionKey);
			EasyMock.expect(encryptionKey.isStatus()).andReturn(true).atLeastOnce();
			replayAll();

			final PrivateKey actualDecryptionKey = getEncryptionService().acquireDecryptionKey(creationDate, state, votingRegion);

			assertNotNull("A decryption key is returned", actualDecryptionKey);
			final RSAPrivateKeySpec actualDecryptionKeySpec = keyFactory.getKeySpec(actualDecryptionKey, RSAPrivateKeySpec.class);
			assertEquals("The modulus is set", privateKeySpec.getModulus(), actualDecryptionKeySpec.getModulus());
			assertEquals("The exponent is set", privateKeySpec.getPrivateExponent(), actualDecryptionKeySpec.getPrivateExponent());
			verifyAll();
		} finally {
			privateKeyFile.delete();
			publicKeyFile.delete();
		}
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.EncryptionService#acquireDecryptionKey(java.util.Date, String, String)} for
	 * the case where another thread creates one while this one is trying to set up to do so.
	 * 
	 * @author IanBrown
	 * 
	 * @throws NoSuchAlgorithmException
	 *             if there is no RSA algorithm available.
	 * @throws InvalidKeySpecException
	 *             if the key specification is invalid.
	 * @throws IOException
	 *             if there is a problem with a key file.
	 * @throws Exception
	 *             if the acquisition fails.
	 * @since Nov 5, 2012
	 * @version Nov 7, 2012
	 */
	@Test
	public final void testAcquireDecryptionKey_insertedByAnotherThread() throws Exception {
		final Date creationDate = new Date();
		final String state = "ST";
		final String votingRegion = "Voting Region";
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(creationDate);
		final int month = calendar.get(Calendar.MONTH);
		final int year = calendar.get(Calendar.YEAR);
		final File folder = new File("target/test-classes");
		getEncryptionService().setKeyFolderPath(folder.getAbsolutePath());
		final String date = year + "" + month;
		final File privateKeyFile = new File(folder, state + "-" + votingRegion + "-" + date + ".dky");
		final File publicKeyFile = new File(folder, state + "-" + votingRegion + "-" + date + ".eky");
		try {
			final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			final KeyPair keyPair = keyPairGenerator.genKeyPair();
			final PrivateKey privateKey = keyPair.getPrivate();
			final PublicKey publicKey = keyPair.getPublic();
			final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			final RSAPrivateKeySpec privateKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
			saveToFile(privateKeyFile, privateKeySpec.getModulus(), privateKeySpec.getPrivateExponent());
			final RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
			saveToFile(publicKeyFile, publicKeySpec.getModulus(), publicKeySpec.getPublicExponent());
			EasyMock.expect(getEncryptionDAO().findByStateVotingRegionAndDate(state, votingRegion, date)).andReturn(null);
			getEncryptionDAO().makePersistent(EasyMock.anyObject());
			EasyMock.expectLastCall().andThrow(new DataIntegrityViolationException("Expected exception"));
			final EncryptionKeyStatus encryptionKey = createMock("EncryptionKey", EncryptionKeyStatus.class);
			EasyMock.expect(getEncryptionDAO().findByStateVotingRegionAndDate(state, votingRegion, date)).andReturn(encryptionKey);
			EasyMock.expect(encryptionKey.isStatus()).andReturn(true).atLeastOnce();
			replayAll();

			final PrivateKey actualDecryptionKey = getEncryptionService().acquireDecryptionKey(creationDate, state, votingRegion);

			assertNotNull("A decryption key is returned", actualDecryptionKey);
			final RSAPrivateKeySpec actualDecryptionKeySpec = keyFactory.getKeySpec(actualDecryptionKey, RSAPrivateKeySpec.class);
			assertEquals("The modulus is set", privateKeySpec.getModulus(), actualDecryptionKeySpec.getModulus());
			assertEquals("The exponent is set", privateKeySpec.getPrivateExponent(), actualDecryptionKeySpec.getPrivateExponent());
			verifyAll();
		} finally {
			privateKeyFile.delete();
			publicKeyFile.delete();
		}
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.EncryptionService#acquireDecryptionKey(java.util.Date, String, String)} for
	 * the case where there is a key created by another thread.
	 * 
	 * @author IanBrown
	 * 
	 * @throws NoSuchAlgorithmException
	 *             if there is no RSA algorithm available.
	 * @throws InvalidKeySpecException
	 *             if the key specification is invalid.
	 * @throws IOException
	 *             if there is a problem with a key file.
	 * @throws Exception
	 *             if the acquisition fails.
	 * @since Nov 5, 2012
	 * @version Nov 7, 2012
	 */
	@Test
	public final void testAcquireDecryptionKey_keyFromAnotherThread() throws Exception {
		final Date creationDate = new Date();
		final String state = "ST";
		final String votingRegion = "Voting Region";
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(creationDate);
		final int month = calendar.get(Calendar.MONTH);
		final int year = calendar.get(Calendar.YEAR);
		final File folder = new File("target/test-classes");
		getEncryptionService().setKeyFolderPath(folder.getAbsolutePath());
		final String date = year + "" + month;
		final File privateKeyFile = new File(folder, state + "-" + votingRegion + "-" + date + ".dky");
		final File publicKeyFile = new File(folder, state + "-" + votingRegion + "-" + date + ".eky");
		try {
			final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			final KeyPair keyPair = keyPairGenerator.genKeyPair();
			final PrivateKey privateKey = keyPair.getPrivate();
			final PublicKey publicKey = keyPair.getPublic();
			final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			final RSAPrivateKeySpec privateKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
			saveToFile(privateKeyFile, privateKeySpec.getModulus(), privateKeySpec.getPrivateExponent());
			final RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
			saveToFile(publicKeyFile, publicKeySpec.getModulus(), publicKeySpec.getPublicExponent());
			final EncryptionKeyStatus encryptionKey = createMock("EncryptionKey", EncryptionKeyStatus.class);
			EasyMock.expect(getEncryptionDAO().findByStateVotingRegionAndDate(state, votingRegion, date)).andReturn(encryptionKey)
					.atLeastOnce();
			EasyMock.expect(encryptionKey.isStatus()).andReturn(false);
			EasyMock.expect(encryptionKey.isStatus()).andReturn(true).atLeastOnce();
			replayAll();

			final PrivateKey actualDecryptionKey = getEncryptionService().acquireDecryptionKey(creationDate, state, votingRegion);

			assertNotNull("A decryption key is returned", actualDecryptionKey);
			final RSAPrivateKeySpec actualDecryptionKeySpec = keyFactory.getKeySpec(actualDecryptionKey, RSAPrivateKeySpec.class);
			assertEquals("The modulus is set", privateKeySpec.getModulus(), actualDecryptionKeySpec.getModulus());
			assertEquals("The exponent is set", privateKeySpec.getPrivateExponent(), actualDecryptionKeySpec.getPrivateExponent());
			verifyAll();
		} finally {
			privateKeyFile.delete();
			publicKeyFile.delete();
		}
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.EncryptionService#acquireDecryptionKey(java.util.Date, String, String)} for
	 * the case where a new one is created.
	 * 
	 * @author IanBrown
	 * 
	 * @throws NoSuchAlgorithmException
	 *             if there is no RSA algorithm available.
	 * @throws InvalidKeySpecException
	 *             if the key specification is invalid.
	 * @throws IOException
	 *             if there is a problem with a key file.
	 * @throws Exception
	 *             if the acquisition fails.
	 * @since Nov 5, 2012
	 * @version Nov 7, 2012
	 */
	@Test
	public final void testAcquireDecryptionKey_newKey() throws Exception {
		final Date creationDate = new Date();
		final String state = "ST";
		final String votingRegion = "Voting Region";
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(creationDate);
		final int month = calendar.get(Calendar.MONTH);
		final int year = calendar.get(Calendar.YEAR);
		final File folder = new File("target/test-classes");
		getEncryptionService().setKeyFolderPath(folder.getAbsolutePath());
		final String date = year + "" + month;
		final File privateKeyFile = new File(folder, state + "-" + votingRegion + "-" + date + ".dky");
		final File publicKeyFile = new File(folder, state + "-" + votingRegion + "-" + date + ".eky");
		try {
			EasyMock.expect(getEncryptionDAO().findByStateVotingRegionAndDate(state, votingRegion, date)).andReturn(null);
			getEncryptionDAO().makePersistent(EasyMock.anyObject());
			EasyMock.expectLastCall().andDelegateTo(new EncryptionKeyStatusDAO() {
				@Override
				public void makePersistent(final Object object) {
					assertTrue("An encryption key status is made persistent", object instanceof EncryptionKeyStatus);
					final EncryptionKeyStatus keyStatus = (EncryptionKeyStatus) object;
					assertFalse("The encryption key status indicates that there is no encryption key yet", keyStatus.isStatus());
				}
			});
			EasyMock.expectLastCall().andDelegateTo(new EncryptionKeyStatusDAO() {
				@Override
				public void makePersistent(final Object object) {
					assertTrue("An encryption key status is made persistent", object instanceof EncryptionKeyStatus);
					final EncryptionKeyStatus keyStatus = (EncryptionKeyStatus) object;
					assertTrue("The encryption key status indicates that there is an encryption key now", keyStatus.isStatus());
				}
			});
			replayAll();

			final PrivateKey actualDecryptionKey = getEncryptionService().acquireDecryptionKey(creationDate, state, votingRegion);

			assertNotNull("A decryption key is returned", actualDecryptionKey);
			assertTrue("There is a private key file", privateKeyFile.exists());
			assertTrue("There is a public key file", publicKeyFile.exists());
			verifyAll();
		} finally {
			privateKeyFile.delete();
			publicKeyFile.delete();
		}
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.EncryptionService#acquireEncryptionKey(java.util.Date, String, String)} for
	 * the case where there is an existing key.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             is there is a problem acquiring the encryption key.
	 * @since Nov 5, 2012
	 * @version Nov 7, 2012
	 */
	@Test
	public final void testAcquireEncryptionKey_existingKey() throws Exception {
		final Date creationDate = new Date();
		final String state = "ST";
		final String votingRegion = "Voting Region";
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(creationDate);
		final int month = calendar.get(Calendar.MONTH);
		final int year = calendar.get(Calendar.YEAR);
		final File folder = new File("target/test-classes");
		getEncryptionService().setKeyFolderPath(folder.getAbsolutePath());
		final String date = year + "" + month;
		final File privateKeyFile = new File(folder, state + "-" + votingRegion + "-" + date + ".dky");
		final File publicKeyFile = new File(folder, state + "-" + votingRegion + "-" + date + ".eky");
		try {
			final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			final KeyPair keyPair = keyPairGenerator.genKeyPair();
			final PrivateKey privateKey = keyPair.getPrivate();
			final PublicKey publicKey = keyPair.getPublic();
			final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			final RSAPrivateKeySpec privateKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
			saveToFile(privateKeyFile, privateKeySpec.getModulus(), privateKeySpec.getPrivateExponent());
			final RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
			saveToFile(publicKeyFile, publicKeySpec.getModulus(), publicKeySpec.getPublicExponent());
			final EncryptionKeyStatus encryptionKey = createMock("EncryptionKey", EncryptionKeyStatus.class);
			EasyMock.expect(getEncryptionDAO().findByStateVotingRegionAndDate(state, votingRegion, date)).andReturn(encryptionKey);
			EasyMock.expect(encryptionKey.isStatus()).andReturn(true).atLeastOnce();
			replayAll();

			final PublicKey actualEncryptionKey = getEncryptionService().acquireEncryptionKey(creationDate, state, votingRegion);

			assertNotNull("An encryption key is returned", actualEncryptionKey);
			final RSAPublicKeySpec actualEncryptionKeySpec = keyFactory.getKeySpec(actualEncryptionKey, RSAPublicKeySpec.class);
			assertEquals("The modulus is set", publicKeySpec.getModulus(), actualEncryptionKeySpec.getModulus());
			assertEquals("The exponent is set", publicKeySpec.getPublicExponent(), actualEncryptionKeySpec.getPublicExponent());
			verifyAll();
		} finally {
			privateKeyFile.delete();
			publicKeyFile.delete();
		}
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.EncryptionService#acquireEncryptionKey(java.util.Date, String, String)} for
	 * the case where there is an existing key.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             is there is a problem acquiring the encryption key.
	 * @since Nov 5, 2012
	 * @version Nov 7, 2012
	 */
	@Test
	public final void testAcquireEncryptionKey_newKey() throws Exception {
		final Date creationDate = new Date();
		final String state = "ST";
		final String votingRegion = "Voting Region";
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(creationDate);
		final int month = calendar.get(Calendar.MONTH);
		final int year = calendar.get(Calendar.YEAR);
		final File folder = new File("target/test-classes");
		getEncryptionService().setKeyFolderPath(folder.getAbsolutePath());
		final String date = year + "" + month;
		final File privateKeyFile = new File(folder, state + "-" + votingRegion + "-" + date + ".dky");
		final File publicKeyFile = new File(folder, state + "-" + votingRegion + "-" + date + ".eky");
		try {
			EasyMock.expect(getEncryptionDAO().findByStateVotingRegionAndDate(state, votingRegion, date)).andReturn(null);
			getEncryptionDAO().makePersistent(EasyMock.anyObject());
			EasyMock.expectLastCall().andDelegateTo(new EncryptionKeyStatusDAO() {
				@Override
				public void makePersistent(final Object object) {
					assertTrue("An encryption key status is made persistent", object instanceof EncryptionKeyStatus);
					final EncryptionKeyStatus keyStatus = (EncryptionKeyStatus) object;
					assertFalse("The encryption key status indicates that there is no encryption key yet", keyStatus.isStatus());
				}
			});
			EasyMock.expectLastCall().andDelegateTo(new EncryptionKeyStatusDAO() {
				@Override
				public void makePersistent(final Object object) {
					assertTrue("An encryption key status is made persistent", object instanceof EncryptionKeyStatus);
					final EncryptionKeyStatus keyStatus = (EncryptionKeyStatus) object;
					assertTrue("The encryption key status indicates that there is an encryption key now", keyStatus.isStatus());
				}
			});
			replayAll();

			final PublicKey actualEncryptionKey = getEncryptionService().acquireEncryptionKey(creationDate, state, votingRegion);

			assertNotNull("An encryption key is returned", actualEncryptionKey);
			assertTrue("There is a private key file", privateKeyFile.exists());
			assertTrue("There is a public key file", publicKeyFile.exists());
			verifyAll();
		} finally {
			privateKeyFile.delete();
			publicKeyFile.delete();
		}
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.EncryptionService#decrypt(java.util.Date, byte[])}.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception if there is a problem encrypting or decrypting the data.
	 * @since Nov 5, 2012
	 * @version Nov 8, 2012
	 */
	@Test
	public final void testDecrypt() throws Exception {
		final Date creationDate = new Date();
		final String state = "ST";
		final String votingRegion = "Voting Region";
		final String string = "String to encrypt";
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(creationDate);
		final int month = calendar.get(Calendar.MONTH);
		final int year = calendar.get(Calendar.YEAR);
		final File folder = new File("target/test-classes");
		getEncryptionService().setKeyFolderPath(folder.getAbsolutePath());
		final String date = year + "" + month;
		final File privateKeyFile = new File(folder, state + "-" + votingRegion + "-" + date + ".dky");
		final File publicKeyFile = new File(folder, state + "-" + votingRegion + "-" + date + ".eky");
		try {
			final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			final KeyPair keyPair = keyPairGenerator.genKeyPair();
			final PrivateKey privateKey = keyPair.getPrivate();
			final PublicKey publicKey = keyPair.getPublic();
			final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			final RSAPrivateKeySpec privateKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
			saveToFile(privateKeyFile, privateKeySpec.getModulus(), privateKeySpec.getPrivateExponent());
			final RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
			saveToFile(publicKeyFile, publicKeySpec.getModulus(), publicKeySpec.getPublicExponent());
			final EncryptionKeyStatus encryptionKey = createMock("EncryptionKey", EncryptionKeyStatus.class);
			EasyMock.expect(getEncryptionDAO().findByStateVotingRegionAndDate(state, votingRegion, date)).andReturn(encryptionKey);
			EasyMock.expect(encryptionKey.isStatus()).andReturn(true).atLeastOnce();
			final Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			final byte[] encryptedBytes = cipher.doFinal(string.getBytes());
			replayAll();
			
			final String actualDecryptedString = getEncryptionService().makeDecryption(creationDate, state, votingRegion, encryptedBytes);
			
			assertEquals("The string decrypts to the input string", string, actualDecryptedString);
			
			verifyAll();
		} finally {
			privateKeyFile.delete();
			publicKeyFile.delete();
		}
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.EncryptionService#makeEncryption(java.util.Date, String, String, java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception if there is a problem encrypting or decrypting the data.
	 * @since Nov 5, 2012
	 * @version Nov 7, 2012
	 */
	@Test
	public final void testEncrypt() throws Exception {
		final Date creationDate = new Date();
		final String state = "ST";
		final String votingRegion = "Voting Region";
		final String string = "String to encrypt";
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(creationDate);
		final int month = calendar.get(Calendar.MONTH);
		final int year = calendar.get(Calendar.YEAR);
		final File folder = new File("target/test-classes");
		getEncryptionService().setKeyFolderPath(folder.getAbsolutePath());
		final String date = year + "" + month;
		final File privateKeyFile = new File(folder, state + "-" + votingRegion + "-" + date + ".dky");
		final File publicKeyFile = new File(folder, state + "-" + votingRegion + "-" + date + ".eky");
		try {
			final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			final KeyPair keyPair = keyPairGenerator.genKeyPair();
			final PrivateKey privateKey = keyPair.getPrivate();
			final PublicKey publicKey = keyPair.getPublic();
			final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			final RSAPrivateKeySpec privateKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
			saveToFile(privateKeyFile, privateKeySpec.getModulus(), privateKeySpec.getPrivateExponent());
			final RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
			saveToFile(publicKeyFile, publicKeySpec.getModulus(), publicKeySpec.getPublicExponent());
			final EncryptionKeyStatus encryptionKey = createMock("EncryptionKey", EncryptionKeyStatus.class);
			EasyMock.expect(getEncryptionDAO().findByStateVotingRegionAndDate(state, votingRegion, date)).andReturn(encryptionKey);
			EasyMock.expect(encryptionKey.isStatus()).andReturn(true).atLeastOnce();
			replayAll();
			
			final byte[] actualEncrypted = getEncryptionService().makeEncryption(creationDate, state, votingRegion, string);
			
			final Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			final byte[] decryptedBytes = cipher.doFinal(actualEncrypted);
			final String decryptedString = new String(decryptedBytes);
			assertEquals("The string decrypts to the input string", string, decryptedString);
			
			verifyAll();
		} finally {
			privateKeyFile.delete();
			publicKeyFile.delete();
		}
	}

	/**
	 * Creates an encryption service.
	 * 
	 * @author IanBrown
	 * @return the encryption service.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private EncryptionService createEncryptionService() {
		return new EncryptionService();
	}

	/**
	 * Gets the encryption DAO.
	 * 
	 * @author IanBrown
	 * @return the encryption DAO.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private EncryptionKeyStatusDAO getEncryptionDAO() {
		return encryptionDAO;
	}

	/**
	 * Gets the encryption service.
	 * 
	 * @author IanBrown
	 * @return the encryption service.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private EncryptionService getEncryptionService() {
		return encryptionService;
	}

	/**
	 * Gets the servlet context.
	 * 
	 * @author IanBrown
	 * @return the servlet context.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * Saves the information for a key to the specified file.
	 * 
	 * @author IanBrown
	 * @param file
	 *            the file.
	 * @param modulus
	 *            the modulo.
	 * @param exponent
	 *            the exponent.
	 * @throws IOException
	 *             if there is a problem saving the key to the file.
	 * @since Nov 5, 2012
	 * @version Nov 7, 2012
	 */
	private void saveToFile(final File file, final BigInteger modulus, final BigInteger exponent) throws IOException {
		final ObjectOutputStream oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		try {
			oout.writeObject(modulus);
			oout.writeObject(exponent);
		} catch (final Exception e) {
			throw new IOException("Unexpected error", e);
		} finally {
			oout.close();
		}
	}

	/**
	 * Sets the encryption DAO.
	 * 
	 * @author IanBrown
	 * @param encryptionDAO
	 *            the encryption DAO to set.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private void setEncryptionDAO(final EncryptionKeyStatusDAO encryptionDAO) {
		this.encryptionDAO = encryptionDAO;
	}

	/**
	 * Sets the encryption service.
	 * 
	 * @author IanBrown
	 * @param encryptionService
	 *            the encryption service to set.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private void setEncryptionService(final EncryptionService encryptionService) {
		this.encryptionService = encryptionService;
	}

	/**
	 * Sets the servlet context.
	 * 
	 * @author IanBrown
	 * @param servletContext
	 *            the servlet context to set.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private void setServletContext(final ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}
