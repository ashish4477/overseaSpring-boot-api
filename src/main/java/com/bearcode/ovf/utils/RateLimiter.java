package com.bearcode.ovf.utils;

/**
 * RateLimiter interface
 * 
 * Support for rate-limits on the number of calls within a certain time interval
 * 
 * @see RateLimiterImpl for implementation
 */
public interface RateLimiter {

	/**
	 * Check rate limits and wait some time if we override limits
	 */
	void checkRates();
	
	/**
	 * Update rate limits
	 */
	void update();
	
	/**
	 * Update rate limits
	 * 
	 * @param totalCalls The number of calls to wait into checkRates method
	 * 
	 */
	void update(int totalCalls);
	
}