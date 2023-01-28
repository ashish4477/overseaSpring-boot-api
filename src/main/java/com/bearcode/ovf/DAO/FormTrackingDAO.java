/**
 * 
 */
package com.bearcode.ovf.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.formtracking.TrackedForm;

/**
 * Extended {@link BearcodeDAO} to provide access to the form tracking data.
 * 
 * @author IanBrown
 * 
 * @since Apr 25, 2012
 * @version Apr 25, 2012
 */
@Repository
public class FormTrackingDAO extends BearcodeDAO {

	/**
	 * Finds all of the tracked forms.
	 * 
	 * @author IanBrown
	 * @return the tracked forms.
	 * @since Apr 25, 2012
	 * @version Apr 25, 2012
	 */
	public List<TrackedForm> findAllTrackedForms() {
		return getHibernateTemplate().loadAll(TrackedForm.class);
	}
}
