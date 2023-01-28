/**
 * 
 */
package com.bearcode.ovf.tools.vip;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import com.bearcode.ovf.tools.vip.xml.VipObject;

/**
 * Interface for objects that acquire resources for {@link VipLoadJob}.
 * 
 * @author IanBrown
 * 
 * @since Jun 25, 2012
 * @version Oct 11, 2012
 */
public interface VipLoadJobValet {

	/**
	 * Acquires an input stream for the specified target.
	 * 
	 * @author IanBrown
	 * @param vipJobTarget
	 *            the target.
	 * @return the input stream.
	 * @exception IOException if there is a problem acquiring the input stream.
	 * @since Jun 25, 2012
	 * @version Aug 17, 2012
	 */
	InputStream acquireInputStream(String vipJobTarget) throws IOException;
	
	/**
	 * Acquires the date that the target was last modified.
	 * @author IanBrown
	 * @param vipJobTarget the target.
	 * @return the last modified date or <code>null</code> if the target cannot be found.
	 * @since Oct 11, 2012
	 * @version Oct 11, 2012
	 */
	Date acquireLastModified(String vipJobTarget);

	/**
	 * Loads a VIP object from the input stream.
	 * 
	 * @author IanBrown
	 * @param is
	 *            the input stream to read.
	 * @return the VIP object.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	VipObject loadVipObject(InputStream is);
}
