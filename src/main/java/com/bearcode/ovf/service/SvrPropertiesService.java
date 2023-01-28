/**
 * 
 */
package com.bearcode.ovf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bearcode.ovf.DAO.SvrPropertiesDAO;
import com.bearcode.ovf.model.svrproperties.SvrProperty;

/**
 * Service to work with {@link SvrProperty} objects.
 * 
 * @author IanBrown
 * 
 * @since Oct 22, 2012
 * @version Oct 22, 2012
 */
@Service
public class SvrPropertiesService {

	/**
	 * the SVR properties DAO.
	 * 
	 * @author IanBrown
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	@Autowired
	private SvrPropertiesDAO svrPropertiesDAO;

	/**
	 * Returns the value of the specified SVR property.
	 * <p>
	 * If the <code>votingRegionName</code> is <code>null</code>, then only the value for the whole state is returned (if any). If
	 * the <code>votingRegionName</code> is not <code>null</code>, then the value for the specific voting region or the whole state
	 * is returned (the first one matched in that order).
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the abbreviation for the state.
	 * @param votingRegionName
	 *            the name of the voting region (may be <code>null</code>).
	 * @param propertyName
	 *            the name of the property.
	 * @return the value of the property.
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	public String findProperty(final String stateAbbreviation, final String votingRegionName, final String propertyName) {
		return getSvrPropertiesDAO().findProperty(stateAbbreviation, votingRegionName, propertyName);
	}

	/**
	 * Gets the SVR properties DAO.
	 * 
	 * @author IanBrown
	 * @return the SVR properties DAO.
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	public SvrPropertiesDAO getSvrPropertiesDAO() {
		return svrPropertiesDAO;
	}

	/**
	 * Sets the SVR properties DAO.
	 * 
	 * @author IanBrown
	 * @param svrPropertiesDAO
	 *            the SVR properties DAO to set.
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	public void setSvrPropertiesDAO(final SvrPropertiesDAO svrPropertiesDAO) {
		this.svrPropertiesDAO = svrPropertiesDAO;
	}
}

