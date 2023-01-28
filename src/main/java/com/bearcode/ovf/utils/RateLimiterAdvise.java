package com.bearcode.ovf.utils;

import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.OvfPropertyService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author leonid.
 *
 * Copied from RateLimiterImpl
 */
public class RateLimiterAdvise implements Ordered {
    private static final float NANO_SECONDS_PER_MILIS_SECONDS = 1000000.0f;

    private double minInterval;

    private double lastTimeCalled = 0.0f;

    private double totalCalls = 1.0f;

    private int order;

    private OvfPropertyService propertyService;

/*
    public RateLimiterAdvise(final int maxCalls, final long perSeconds) {
        long millis = TimeUnit.SECONDS.toMillis(perSeconds);
        this.minInterval = NANO_SECONDS_PER_MILIS_SECONDS * ((double)millis / (double)(maxCalls));
    }
*/
    @PostConstruct
    public void initialize() {
        String maxCallsStr = propertyService.getProperty( OvfPropertyNames.EMAIL_SERVICE_RATE_PER_SECOND );
        String perSecondsStr = propertyService.getProperty( OvfPropertyNames.EMAIL_SERVICE_RATE_PER_SECOND );
        int maxCalls = Integer.parseInt( maxCallsStr );
        long perSeconds = Long.parseLong( perSecondsStr );
        long millis = TimeUnit.SECONDS.toMillis(perSeconds);
        this.minInterval = NANO_SECONDS_PER_MILIS_SECONDS * ((double)millis / (double)(maxCalls));
    }

    public synchronized void checkRates() {
        double elapsed = System.nanoTime() - lastTimeCalled;
        double wait = (totalCalls * minInterval) - elapsed;
        if (wait > 0) {
            sleep(wait);
        }
    }

    public synchronized void update() {
        this.update(1);
    }

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

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder( int order ) {
        this.order = order;
    }

    public OvfPropertyService getPropertyService() {
        return propertyService;
    }

    public void setPropertyService( OvfPropertyService propertyService ) {
        this.propertyService = propertyService;
    }

    public Object aroundHtmlEmailSend( ProceedingJoinPoint pjp ) throws Throwable {
        Object res;
        try {
            checkRates();
            res = pjp.proceed();
        } catch ( Throwable ex ) {
            throw ex;
        }
        finally {
            update();
        }
        return res;
    }
}
