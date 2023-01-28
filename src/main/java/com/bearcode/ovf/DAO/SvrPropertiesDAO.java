/**
 * 
 */
package com.bearcode.ovf.DAO;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.svrproperties.SvrProperty;

/**
 * Extended {@link BearcodeDAO} data access object for working with {@link SvrProperty} objects.
 * 
 * @author IanBrown
 * 
 * @since Oct 22, 2012
 * @version Oct 22, 2012
 */
@Repository
public class SvrPropertiesDAO extends BearcodeDAO {

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
	@SuppressWarnings("unchecked")
	public String findProperty(final String stateAbbreviation, final String votingRegionName, final String propertyName) {
		final Criteria criteria = getSession().createCriteria(SvrProperty.class);
		criteria.add(Restrictions.eq("stateAbbreviation", stateAbbreviation));
		if (votingRegionName == null) {
			criteria.add(Restrictions.isNull("votingRegionName"));
		} else {
			criteria.add(Restrictions.or(Restrictions.eq("votingRegionName", votingRegionName), Restrictions.isNull("votingRegionName")));
		}
		criteria.add(Restrictions.eq("propertyName", propertyName));
		criteria.addOrder(Order.desc("votingRegionName"));
		final List<SvrProperty> properties = criteria.list();
		return properties == null || properties.isEmpty() ? null : properties.get(0).getPropertyValue();
	}

}
