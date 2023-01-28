/**
 * 
 */
package com.bearcode.ovf.utils;

import org.springframework.stereotype.Component;

/**
 * Bean to hold global configuration values.
 * 
 * @author IanBrown
 * 
 * @since Apr 4, 2012
 * @version Apr 4, 2012
 */
@Component
public class OvfConfiguration {

	/**
	 * the deployment environment.
	 * 
	 * @author IanBrown
	 * @since Apr 4, 2012
	 * @version Apr 4, 2012
	 */
	private String deploymentEnv;

	/**
	 * Gets the deployment environment.
	 * 
	 * @author IanBrown
	 * @return the deployment environment.
	 * @since Apr 4, 2012
	 * @version Apr 4, 2012
	 */
	public String getDeploymentEnv() {
		return deploymentEnv;
	}

	/**
	 * Sets the deployment environment.
	 * 
	 * @author IanBrown
	 * @param deploymentEnv
	 *            the deployment environment to set.
	 * @since Apr 4, 2012
	 * @version Apr 4, 2012
	 */
	public void setDeploymentEnv(final String deploymentEnv) {
		this.deploymentEnv = deploymentEnv;
	}
}
