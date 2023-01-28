package com.bearcode.ovf.utils;

import java.util.concurrent.TimeUnit;

/**
 * 
 * Support for rate-limits on the number of calls within a certain time interval
 *
 */
class RateLimiterImpl implements RateLimiter {

	private static final float NANO_SECONDS_PER_MILIS_SECONDS = 1000000.0f;

	private final double minInterval;
	
	private double lastTimeCalled = 0.0f;
	
	private double totalCalls = 1.0f; 
	
	public RateLimiterImpl(final int maxCalls, final long perSeconds) {
		long millis = TimeUnit.SECONDS.toMillis(perSeconds);
		this.minInterval = NANO_SECONDS_PER_MILIS_SECONDS * ((double)millis / (double)(maxCalls));
	}
	
	@Override
	public synchronized void checkRates() {
		double elapsed = System.nanoTime() - lastTimeCalled;
		double wait = (totalCalls * minInterval) - elapsed;
		if (wait > 0) {
			sleep(wait);
		}
	}

	@Override
	public synchronized void update() {
		this.update(1);
	}
	
	@Override
	public synchronized void update(int totalCalls) {
		if (totalCalls < 0) {
			totalCalls = 0;
		}
		this.totalCalls = totalCalls;
		this.lastTimeCalled = System.nanoTime();
	}

	private void sleep(double wait) {
		try {
			long millis = 0L;
			int nanos = 0;
			if (wait > 999999) {
				millis = (long) Math.floor(wait / NANO_SECONDS_PER_MILIS_SECONDS);
				nanos = (int) Math.floor(wait % NANO_SECONDS_PER_MILIS_SECONDS);
			} else {
				nanos = (int)(Math.floor(wait));
			}
			Thread.sleep(millis, nanos);
		} catch (InterruptedException e) {
		}
	}
}

