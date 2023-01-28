package com.bearcode.ovf.utils;

import java.util.HashMap;
import java.util.Map;

public class RateLimiterFactory {
	private static final Map<String, RateLimiter> id2limiters = new HashMap<String, RateLimiter>();
	
	/**
	 * Get/or create Rate Limiter with specific id and limits 
	 * 
	 * @param id unique identifier of Rate Limiter
	 * @param maxCalls max available calls per specific time
	 * @param perSeconds rate limiter time
	 * 
	 * @return instance of Rate Limiter
	 */
	public synchronized static RateLimiter getRateLimiter(final String id, final int maxCalls, final long perSeconds) {
		final String rlid = String.format("%s.%d.%d", id, maxCalls, perSeconds);
		
		RateLimiter rl = id2limiters.get(rlid);
		if (rl != null)
			return rl;
		
		rl = new RateLimiterImpl(maxCalls, perSeconds);
		id2limiters.put(rlid, rl);
		return rl;
	}
}
