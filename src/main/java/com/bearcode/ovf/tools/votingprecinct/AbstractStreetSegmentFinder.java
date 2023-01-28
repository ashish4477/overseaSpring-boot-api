/**
 * 
 */
package com.bearcode.ovf.tools.votingprecinct;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.jgeocoder.AddressComponent;
import net.sourceforge.jgeocoder.us.AddressParser;
import net.sourceforge.jgeocoder.us.AddressStandardizer;

import org.springframework.beans.factory.annotation.Autowired;

import com.bearcode.ovf.DAO.StateDAO;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.vip.VipLocality;
import com.bearcode.ovf.model.vip.VipPrecinct;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.tools.AbstractForStateOrVotingRegion;

/**
 * Abstract base implementation of {@link StreetSegmentFinder}.
 * 
 * @author IanBrown
 * 
 * @since Jul 5, 2012
 * @version Oct 9, 2012
 */
public abstract class AbstractStreetSegmentFinder extends AbstractForStateOrVotingRegion implements StreetSegmentFinder {

	/**
	 * Extracts the street from the normalized address. The street is split into pre direction, street, type, and post direction
	 * parts.
	 * 
	 * @author IanBrown
	 * @param normalizedAddress
	 *            the normalized address.
	 * @return the street string.
	 * @since Jun 4, 2012
	 * @version Jul 6, 2012
	 */
	protected final static String extractStreet(final Map<AddressComponent, String> normalizedAddress) {
		final StringBuilder sb = new StringBuilder("");
		String prefix = "";
		final String predir = normalizedAddress.get(AddressComponent.PREDIR);
		if (predir != null) {
			sb.append(prefix).append(predir);
			prefix = " ";
		}
		sb.append(prefix).append(normalizedAddress.get(AddressComponent.STREET));
		prefix = " ";
		sb.append(prefix).append(normalizedAddress.get(AddressComponent.TYPE));
		final String postdir = normalizedAddress.get(AddressComponent.POSTDIR);
		if (postdir != null) {
			sb.append(prefix).append(postdir);
		}
		return sb.toString();
	}

	/**
	 * Normalizes the address.
	 * 
	 * @author IanBrown
	 * @param address
	 *            the address to normalize.
	 * @param needNormalization
	 *            is address normalization needed or just parsing?
	 * @return the normalized address.
	 * @since Jul 5, 2012
	 * @version Oct 5, 2012
	 */
	protected final static Map<AddressComponent, String> normalizeAddress(final UserAddress address, final boolean needNormalization) {
		final String baseLineAddress = address.getSingleLineAddress().toUpperCase();
		final String lineAddress = baseLineAddress.endsWith(",") ? baseLineAddress.substring(0, baseLineAddress.length() - 1)
				: baseLineAddress;
		final Map<AddressComponent, String> parsedAddress = AddressParser.parseAddress(lineAddress);
		if (parsedAddress == null) {
			return null;
		}

		final Map<AddressComponent, String> normalizedAddress;
		if (needNormalization) {
			normalizedAddress = AddressStandardizer.normalizeParsedAddress(parsedAddress);
			if (normalizedAddress == null) {
				return null;
			}
		} else {
			normalizedAddress = parsedAddress;
		}
		return normalizedAddress;
	}

	/**
	 * do addresses need to be normalized?
	 * 
	 * @author IanBrown
	 * @since Oct 5, 2012
	 * @version Oct 5, 2012
	 */
	private boolean needNormalization;

	/** {@inheritDoc} */
	@Override
	public void fixAddress(final UserAddress address, final VipStreetSegment streetSegment) {
		final String lineAddress = address.getSingleLineAddress();
		final Map<AddressComponent, String> parsedAddress = AddressParser.parseAddress(lineAddress);
		if (parsedAddress == null) {
			return;
		}

		address.setStreet1(parsedAddress.get(AddressComponent.NUMBER) + " " + streetSegment.getNonHouseAddress().toStreet());
		address.setCity(streetSegment.getNonHouseAddress().getCity());
		final VipPrecinct precinct = streetSegment.getPrecinct();
		if (precinct != null) {
			final VipLocality locality = precinct.getLocality();
			if (locality != null && locality.getType().equalsIgnoreCase("COUNTY")) {
				address.setCounty(locality.getName() + " County");
			}
		}
		address.setState(parsedAddress.get(AddressComponent.STATE));
		address.setZip(streetSegment.getNonHouseAddress().getZip());
	}

	/** {@inheritDoc} */
	@Override
	public boolean isNeedNormalization() {
		return needNormalization;
	}

	/** {@inheritDoc} */
	@Override
	public void setNeedNormalization(final boolean needNormalization) {
		this.needNormalization = needNormalization;
	}
}