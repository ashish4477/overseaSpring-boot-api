/**
 * 
 */
package com.bearcode.ovf.tools;

import com.bearcode.ovf.service.email.Email;

/**
 * Interface for objects that acquire resources for {@link ScheduledReportsEmailJob}.
 * 
 * @author IanBrown
 * 
 * @since Mar 8, 2012
 * @version Mar 8, 2012
 */
public interface ScheduledReportsEmailJobValet {

	/**
	 * Acquires a mail command.
	 * 
	 * @author IanBrown
	 * @return the mail command.
	 * @since Mar 8, 2012
	 * @version Mar 8, 2012
	 */
	Email acquireEmail();
}
