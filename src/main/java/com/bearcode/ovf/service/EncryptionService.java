/**
 * 
 */
package com.bearcode.ovf.service;

import com.bearcode.ovf.DAO.EncryptionKeyStatusDAO;
import com.bearcode.ovf.model.encryption.EncryptionKeyStatus;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;

/**
 * Service to perform encryption.
 * 
 * @author IanBrown
 * 
 * @since Nov 5, 2012
 * @version Dec 7, 2012
 */
@Service
public class EncryptionService {
	
	/**
	 * the logger for the class.
	 * @author IanBrown
	 * @since Dec 7, 2012
	 * @version Dec 7, 2012
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(EncryptionService.class);
	
	/**
	 * the calendar used to extract dates.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private final GregorianCalendar calendar = new GregorianCalendar();

	/**
	 * the DAO used to access the encryption keys.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	@Autowired
	private EncryptionKeyStatusDAO encryptionDAO;

	/**
	 * the path to the key folder.
	 * 
	 * @author IanBrown
	 * @since Nov 7, 2012
	 * @version Nov 7, 2012
	 */
    @Value("${encryption.keyFolderPath}")
	private String keyFolderPath;

	/**
	 * the known keys by date.state[.votingRegion].
	 * 
	 * @author IanBrown
	 * @since Nov 7, 2012
	 * @version Nov 7, 2012
	 */
	private final Map<String, Pair<PublicKey, PrivateKey>> knownKeys = new HashMap<String, Pair<PublicKey, PrivateKey>>();

	/**
	 * Acquires the appropriate decryption key for the specified date.
	 * 
	 * @author IanBrown
	 * @param creationDate
	 *            the date.
	 * @param state
	 *            the state abbreviation.
	 * @param votingRegion
	 *            the optional voting region.
	 * @return the decryption key.
	 * @throws Exception
	 *             if this operation fails.
	 * @since Nov 5, 2012
	 * @version Nov 7, 2012
	 */
	public PrivateKey acquireDecryptionKey(final Date creationDate, final String state, final String votingRegion) throws Exception {
		final Pair<PublicKey, PrivateKey> keyPair = acquireKeyPair(creationDate, state, votingRegion);
		return keyPair.getRight();
	}

	/**
	 * Acquires the appropriate encryption key for the specified date.
	 * 
	 * @author IanBrown
	 * @param creationDate
	 *            the date.
	 * @param state
	 *            the state abbreviation.
	 * @param votingRegion
	 *            the optional voting region.
	 * @return the encryption key.
	 * @throws Exception
	 *             if there is a problem acquiring the encryption key.
	 * @since Nov 5, 2012
	 * @version Nov 7, 2012
	 */
	public PublicKey acquireEncryptionKey(final Date creationDate, final String state, final String votingRegion) throws Exception {
		final Pair<PublicKey, PrivateKey> keyPair = acquireKeyPair(creationDate, state, votingRegion);
		return keyPair.getLeft();
	}

	/**
	 * Builds the path to the key file for the state, voting region, date string, and extension.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param votingRegion
	 *            the voting region.
	 * @param date
	 *            the date string.
	 * @param extension
	 *            the extension for the key file.
	 * @return the path to the key file.
	 * @since Nov 7, 2012
	 * @version Nov 7, 2012
	 */
	public String buildKeyFilePath(final String state, final String votingRegion, final String date, final String extension) {
		return state + "-" + (votingRegion == null ? "" : votingRegion + "-") + date + extension;
	}

	/**
	 * Decrypts the data based on the specified creation date, state and voting region.
	 * 
	 * @author IanBrown
	 * @param creationDate
	 *            the creation date.
	 * @param state
	 *            the state.
	 * @param votingRegion
	 *            the voting region.
	 * @param data
	 *            the encrypted data.
	 * @return the decrypted string.
	 * @throws Exception
	 *             if there is a problem decrypting the data.
	 * @since Nov 5, 2012
	 * @version Nov 8, 2012
	 */
	public String makeDecryption(final Date creationDate, final String state, final String votingRegion, final byte[] data)
			throws Exception {
		final PrivateKey decryptionKey = acquireDecryptionKey(creationDate, state, votingRegion);
		final Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, decryptionKey);
		return new String(cipher.doFinal(data));
	}

	/**
	 * Encrypts the string based on the specified creation date, state, and voting region.
	 * 
	 * @author IanBrown
	 * @param creationDate
	 *            the creation date.
	 * @param state
	 *            the state.
	 * @param votingRegion
	 *            the optional voting region.
	 * @param string
	 *            the string.
	 * @return the encrypted data.
	 * @throws Exception
	 *             if there is a problem encrypting the data.
	 * @since Nov 5, 2012
	 * @version Nov 7, 2012
	 */
	public byte[] makeEncryption(final Date creationDate, final String state, final String votingRegion, final String string)
			throws Exception {
		final PublicKey encryptionKey = acquireEncryptionKey(creationDate, state, votingRegion);
		final Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
		return cipher.doFinal(string.getBytes());
	}

	/**
	 * Gets the encryption DAO.
	 * 
	 * @author IanBrown
	 * @return the encryption DAO.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	public EncryptionKeyStatusDAO getEncryptionDAO() {
		return encryptionDAO;
	}

	/**
	 * Gets the key folder path.
	 * 
	 * @author IanBrown
	 * @return the key folder path.
	 * @since Nov 7, 2012
	 * @version Nov 7, 2012
	 */
	public String getKeyFolderPath() {
		return keyFolderPath;
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
	public void setEncryptionDAO(final EncryptionKeyStatusDAO encryptionDAO) {
		this.encryptionDAO = encryptionDAO;
	}

	/**
	 * Sets the key folder path.
	 * 
	 * @author IanBrown
	 * @param keyFolderPath
	 *            the key folder path to set.
	 * @since Nov 7, 2012
	 * @version Nov 7, 2012
	 */
	public void setKeyFolderPath(final String keyFolderPath) {
		this.keyFolderPath = keyFolderPath;
	}

	/**
	 * Acquires the status of the encryption key.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param votingRegion
	 *            the optional voting region.
	 * @param date
	 *            the date string.
	 * @return the status of the encryption key.
	 * @throws InterruptedException
	 *             if this operation was interrupted.
	 * @since Nov 7, 2012
	 * @version Dec 7, 2012
	 */
	private EncryptionKeyStatus acquireEncryptionKeyStatus(final String state, final String votingRegion, final String date)
			throws InterruptedException {
		EncryptionKeyStatus keyStatus = null;

		while (true) {
			keyStatus = getEncryptionDAO().findByStateVotingRegionAndDate(state, votingRegion, date);

			if (keyStatus == null) {
				// There is no existing key - try and take control of key creation.
				keyStatus = new EncryptionKeyStatus();
				keyStatus.setState(state);
				keyStatus.setVotingRegion(votingRegion);
				keyStatus.setDate(date);
				keyStatus.setStatus(false);
				try {
					// If we can persist the key status, then we got control and we'll make the key pair.
					getEncryptionDAO().makePersistent(keyStatus);
					break;
				} catch (final DataIntegrityViolationException e) {
					// Some other thread already took charge - we'll wait for it to create the desired key pair.
					LOGGER.warn("Encryption key status error", e);
					Thread.sleep(100l);
				}

			} else if (keyStatus.isStatus()) {
				// There is an existing key stored.
				break;

			} else {
				// Another thread is building the key, so wait a little while and try again.
				LOGGER.debug("Waiting for key");
				Thread.sleep(100l);
			}
		}

		return keyStatus;
	}

	/**
	 * Acquires the key pair for the date, state, and voting region.
	 * 
	 * @author IanBrown
	 * @param creationDate
	 *            the creation date.
	 * @param state
	 *            the state.
	 * @param votingRegion
	 *            the voting region.
	 * @return the key pair.
	 * @throws Exception
	 *             if this operation fails.
	 * @since Nov 7, 2012
	 * @version Nov 7, 2012
	 */
	private Pair<PublicKey, PrivateKey> acquireKeyPair(final Date creationDate, final String state, final String votingRegion)
			throws Exception {
		final String date = extractDate(creationDate);
		final String keyTag = date + "." + state + (votingRegion == null ? "" : "." + votingRegion);
		synchronized (knownKeys) {
			Pair<PublicKey, PrivateKey> keyPair = knownKeys.get(keyTag);
			if (keyPair == null) {
				final EncryptionKeyStatus keyStatus = acquireEncryptionKeyStatus(state, votingRegion, date);
				if (keyStatus.isStatus()) {
					keyPair = loadKeyPair(state, votingRegion, date);
				} else {
					keyPair = createKeyPair(keyStatus, state, votingRegion, date);
				}
				knownKeys.put(keyTag, keyPair);
			}
			return keyPair;
		}
	}

	/**
	 * Creates a key pair for the specified state, voting region, and date string.
	 * 
	 * @author IanBrown
	 * @param keyStatus
	 *            the key status.
	 * @param state
	 *            the state.
	 * @param votingRegion
	 *            the voting region.
	 * @param date
	 *            the date string.
	 * @return the key pair.
	 * @throws NoSuchAlgorithmException
	 *             if the RSA algorithm is not supported.
	 * @throws InvalidKeySpecException
	 *             if the key specification is not valid.
	 * @throws IOException
	 *             if there is a problem writing the key files.
	 * @since Nov 7, 2012
	 * @version Dec 7, 2012
	 */
	private Pair<PublicKey, PrivateKey> createKeyPair(final EncryptionKeyStatus keyStatus, final String state,
			final String votingRegion, final String date) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		final File folder = new File(getKeyFolderPath());
		final File privateKeyFile = new File(folder, buildKeyFilePath(state, votingRegion, date, ".dky"));
		final File publicKeyFile = new File(folder, buildKeyFilePath(state, votingRegion, date, ".eky"));
		try {
			final KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			final KeyPair kp = kpg.genKeyPair();
			final PublicKey publicKey = kp.getPublic();
			final PrivateKey privateKey = kp.getPrivate();
			final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			final RSAPrivateKeySpec privateKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
			saveToFile(privateKeyFile, privateKeySpec.getModulus(), privateKeySpec.getPrivateExponent());
			final RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
			saveToFile(publicKeyFile, publicKeySpec.getModulus(), publicKeySpec.getPublicExponent());
			keyStatus.setStatus(true);
			getEncryptionDAO().makePersistent(keyStatus);
			return Pair.of(publicKey, privateKey);
		} finally {
			if (!keyStatus.isStatus()) {
				privateKeyFile.delete();
				publicKeyFile.delete();
				getEncryptionDAO().makeTransient(keyStatus);
			}
		}
	}

	/**
	 * Extracts a date string from the input creation date.
	 * 
	 * @author IanBrown
	 * @param creationDate
	 *            the creation date.
	 * @return the extracted date.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private String extractDate(final Date creationDate) {
		calendar.setTime(creationDate);
		final String date = calendar.get(Calendar.YEAR) + "" + calendar.get(Calendar.MONTH);
		return date;
	}

	/**
	 * Loads the decryption for the state, voting region, and date string.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param votingRegion
	 *            the voting region.
	 * @param date
	 *            the date string.
	 * @return the decryption key.
	 * @throws ClassNotFoundException
	 *             if there is a problem with a class.
	 * @throws IOException
	 *             if there is a problem reading the keys.
	 * @throws NoSuchAlgorithmException
	 *             if the RSA algorithm isn't supported.
	 * @throws InvalidKeySpecException
	 *             if the key isn't specified correctly.
	 * @since Nov 7, 2012
	 * @version Nov 7, 2012
	 */
	private PrivateKey loadDecryptionKey(final String state, final String votingRegion, final String date) throws IOException,
			ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {
		final Pair<BigInteger, BigInteger> keyValues = loadKeyValues(state, votingRegion, date, ".dky");
		final RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(keyValues.getLeft(), keyValues.getRight());
		final KeyFactory factory = KeyFactory.getInstance("RSA");
		final PrivateKey decryptionKey = factory.generatePrivate(keySpec);
		return decryptionKey;
	}

	/**
	 * Loads the encryption key for the state, voting region, and date string.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param votingRegion
	 *            the voting region.
	 * @param date
	 *            the date string.
	 * @return the encryption key.
	 * @throws ClassNotFoundException
	 *             if there is a problem with a class.
	 * @throws IOException
	 *             if there is a problem reading the keys.
	 * @throws NoSuchAlgorithmException
	 *             if the RSA algorithm isn't supported.
	 * @throws InvalidKeySpecException
	 *             if the key isn't specified correctly.
	 * @since Nov 7, 2012
	 * @version Nov 7, 2012
	 */
	private PublicKey loadEncryptionKey(final String state, final String votingRegion, final String date) throws IOException,
			ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {
		final Pair<BigInteger, BigInteger> keyValues = loadKeyValues(state, votingRegion, date, ".eky");
		final RSAPublicKeySpec keySpec = new RSAPublicKeySpec(keyValues.getLeft(), keyValues.getRight());
		final KeyFactory factory = KeyFactory.getInstance("RSA");
		final PublicKey encryptionKey = factory.generatePublic(keySpec);
		return encryptionKey;
	}

	/**
	 * Loads the key pair for the specified state, voting region, and date string.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param votingRegion
	 *            the optional voting region.
	 * @param date
	 *            the date string.
	 * @return the key pair.
	 * @throws ClassNotFoundException
	 *             if there is a problem with a class.
	 * @throws IOException
	 *             if there is a problem reading the keys.
	 * @throws NoSuchAlgorithmException
	 *             if the RSA algorithm isn't supported.
	 * @throws InvalidKeySpecException
	 *             if the key isn't specified correctly.
	 * @since Nov 7, 2012
	 * @version Nov 7, 2012
	 */
	private Pair<PublicKey, PrivateKey> loadKeyPair(final String state, final String votingRegion, final String date)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, ClassNotFoundException {
		final PublicKey encryptionKey = loadEncryptionKey(state, votingRegion, date);
		final PrivateKey decryptionKey = loadDecryptionKey(state, votingRegion, date);
		final Pair<PublicKey, PrivateKey> keyPair = Pair.of(encryptionKey, decryptionKey);
		return keyPair;
	}

	/**
	 * Loads the key values (modulus and exponent) for the specified state, voting region, date string, and file extension.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param votingRegion
	 *            the voting region.
	 * @param date
	 *            the date string.
	 * @param extension
	 *            the file extension.
	 * @return the key values.
	 * @throws IOException
	 *             if there is a problem loading the key values.
	 * @throws ClassNotFoundException
	 *             if there is a problem loading a key value.
	 * @since Nov 7, 2012
	 * @version Nov 7, 2012
	 */
	private Pair<BigInteger, BigInteger> loadKeyValues(final String state, final String votingRegion, final String date,
			final String extension) throws IOException, ClassNotFoundException {
		final File folder = new File(getKeyFolderPath());
		final File keyFile = new File(folder, buildKeyFilePath(state, votingRegion, date, extension));
		final ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(keyFile)));
		try {
			final BigInteger modulus = (BigInteger) ois.readObject();
			final BigInteger exponent = (BigInteger) ois.readObject();
			return Pair.of(modulus, exponent);
		} finally {
			ois.close();
		}
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
        if ( !file.exists() ) {
            final File dir = file.getParentFile();
            if ( dir.exists() || dir.mkdir() ) {
                if ( !file.createNewFile() ) {
                    throw new IOException("Couldn't create file " + file.getAbsolutePath());
                }
            }
        }
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
}
